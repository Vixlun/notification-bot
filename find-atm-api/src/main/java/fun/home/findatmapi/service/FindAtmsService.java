package fun.home.findatmapi.service;

import fun.home.findatmapi.model.Atm;
import fun.home.findatmapi.model.AtmRequest;

import java.util.List;

public interface FindAtmsService {
    List<Atm> findAtmsWithCurrency(AtmRequest request);
}
