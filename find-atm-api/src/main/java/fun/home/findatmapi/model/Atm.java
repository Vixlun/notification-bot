package fun.home.findatmapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class Atm {
    private String id;
    private String address;
    private String installPlace;
    private AtmInfo atmInfo;
    @ToString.Exclude
    private Coordinate location;

    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AtmInfo {
        boolean available;
        List<Limit> limits;
    }

    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Limit {
        Currency currency;
        int amount;
        int withdrawMaxAmount;
        int depositionMaxAmount;
        List<Integer> withdrawDenominations;
    }
}
