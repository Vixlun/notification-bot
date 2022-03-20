package fun.home.findatmapi.service;

import fun.home.findatmapi.model.Atm;

import java.util.List;

public interface AtmDeserializerService {
    List<Atm> deserializeJsonToAtmList(String json);
}
