package fun.home.findatmapi.service;

import fun.home.findatmapi.model.Atm;

public interface AtmFilterService {
    boolean matchWithFilter(Atm atm);
}
