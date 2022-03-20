package fun.home.findatmapi.service.impl;

import fun.home.findatmapi.model.Atm;
import fun.home.findatmapi.model.Currency;
import fun.home.findatmapi.service.AtmFilterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AtmFilterServiceImpl implements AtmFilterService {
    @Value("${atm.min.amount}")
    private int minAmountInAtm;

    @Override
    public boolean matchWithFilter(Atm atm) {
        if(atm.getAtmInfo() != null) {
            atm.getAtmInfo().setLimits(atm.getAtmInfo().getLimits().stream().filter(limit -> {
                boolean isForeignCurrency = limit.getCurrency() == Currency.USD || limit.getCurrency() == Currency.EUR;
                boolean isEnoughMoney = limit.getAmount() > minAmountInAtm;
                return isForeignCurrency && isEnoughMoney;
            }).collect(Collectors.toList()));
            return !atm.getAtmInfo().getLimits().isEmpty();
        } else {
            return false;
        }
    }
}
