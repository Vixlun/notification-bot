package fun.home.findatmapi.service.impl;

import fun.home.findatmapi.model.Atm;
import fun.home.findatmapi.model.AtmRequest;
import fun.home.findatmapi.service.AtmDeserializerService;
import fun.home.findatmapi.service.AtmFilterService;
import fun.home.findatmapi.service.FindAtmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FindAtmsServiceImpl implements FindAtmsService {
    @Value("${tinkoff.api.uri}")
    private String uri;

    @Autowired
    private WebClient webClient;

    @Autowired
    private AtmDeserializerService deserializerService;

    @Autowired
    private AtmFilterService filterAtmService;

    @Override
    public List<Atm> findAtmsWithCurrency(AtmRequest request) {
        return Objects.requireNonNull(webClient.post()
                        .uri(uri)
                        .accept(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .retrieve()
                        .bodyToMono(String.class)
                        .map(json -> deserializerService.deserializeJsonToAtmList(json))
                        .map(atms -> atms.stream().filter(atm -> filterAtmService.matchWithFilter(atm)).collect(Collectors.toList()))
                        .block());
    }
}
