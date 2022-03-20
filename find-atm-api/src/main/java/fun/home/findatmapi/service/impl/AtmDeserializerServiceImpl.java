package fun.home.findatmapi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fun.home.findatmapi.model.Atm;
import fun.home.findatmapi.service.AtmDeserializerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AtmDeserializerServiceImpl implements AtmDeserializerService {

    public List<Atm> deserializeJsonToAtmList(String json) {
        try {
            ObjectMapper om = new ObjectMapper();
            JsonNode clusters = om.readTree(json).path("payload").path("clusters");
            List<Atm> result = new ArrayList<>();
            for(JsonNode clusterAtm : clusters) {
                for(JsonNode atmPoint : clusterAtm.path("points")) {
                    try {
                        Atm atm = om.treeToValue(atmPoint, Atm.class);
                        result.add(atm);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
