package fun.home.findatmapi.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Filter {
    private List<String> banks;
    private boolean showUnavailable;
    private List<Currency> currencies;
}
