package telegram.bot.items;

import fun.home.findatmapi.model.Coordinate;
import org.springframework.context.annotation.Bean;

public enum Sector {
    SOKOL(55.79294535627852, 37.51236419478393, 55.81513419127517, 37.539486692342514),
    KOLOMENSKAYA(55.67630350916136, 37.66262013987026, 55.69855904077647, 37.68974263742885),
    KUNTSEVO(55.71490595387961, 37.397036480776435, 55.75936017873099, 37.46012203680671),
    CENTER(55.713485772314996, 37.563767316020765, 55.802346636061806, 37.68993842808132);

    private double bottomLeftLat;
    private double bottomLeftLng;
    private double topRightLat;
    private double topRightLng;

    Sector(double bottomLeftLat, double bottomLeftLng, double topRightLat, double topRightLng) {
        this.bottomLeftLat = bottomLeftLat;
        this.bottomLeftLng = bottomLeftLng;
        this.topRightLat = topRightLat;
        this.topRightLng = topRightLng;
    }

    public static Sector findForCoordinates(Coordinate location) {
        for(Sector sector : values()) {
            boolean inLatRange = location.getLat() > sector.bottomLeftLat && location.getLat() < sector.topRightLat;
            boolean inLngRange = location.getLng() > sector.bottomLeftLng && location.getLng() < sector.topRightLng;
            if(inLatRange && inLngRange) {
                return sector;
            }
        }
        return null;
    }
}
