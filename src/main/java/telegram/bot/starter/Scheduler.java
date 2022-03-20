package telegram.bot.starter;

import fun.home.findatmapi.model.*;
import fun.home.findatmapi.service.FindAtmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import telegram.bot.demo.NotificationBot;
import telegram.bot.items.Sector;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@EnableScheduling
public class Scheduler {
    @Autowired
    private FindAtmsService findService;
    @Autowired
    private NotificationBot notificationService;

    private final Map<String, Atm> atmCacheMap = new ConcurrentHashMap<>();

    @Scheduled(fixedDelayString = "${refresh.period}")
    public void checkAtmWithCurrency() {
        log.trace("Handle scheduler for checking ATM with currency");
        List<Atm> atmList = findService.findAtmsWithCurrency(getModelRequest());
        for(Atm atm : atmList) {
            if(!atmCacheMap.containsKey(atm.getId())) {
                log.debug("Show new ATM: {}", atm);
                Sector sector = Sector.findForCoordinates(atm.getLocation());
                if(sector != null) {
                    log.debug("Find ATM({}) in '{}' district", atm, sector);
                    notificationService.sendToClientsNotification(atm.toString(), sector);
                } else {
                    log.debug("ATM({}) with location = '{}' out of sectors.", atm.getId(), atm.getLocation());
                }
                atmCacheMap.put(atm.getId(), atm);
            }
        }
    }

    @Scheduled(cron = "${evict.cache.cron}")
    public void evictAtmCache() {
        log.info("Clear cache");
        atmCacheMap.clear();
    }

    private AtmRequest getModelRequest() {
//        Coordinate bottomLeft = new Coordinate(55.79294535627852, 37.51236419478393);
//        Coordinate topRight = new Coordinate(55.81513419127517, 37.539486692342514);
        Bounds moscowDistrict = new Bounds(55.57987943751137, 37.36758835150007, 55.935322927624654, 37.87227279974227);
        boolean showUnavailableAtm = false;
        int zoomCount = 11;
        String tinkoffShortName = "tcs";
        return AtmRequest.builder()
                .bounds(moscowDistrict)
                .filters(new Filter(List.of(tinkoffShortName), showUnavailableAtm, List.of(Currency.USD)))
                .zoom(zoomCount)
                .build();
    }
}
