package prise.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import prise.example.demo.model.ShellyEclaiData;
import prise.example.demo.repository.ShellyEclaiDataRepository;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ShellyEclaiService {

    private static final Logger logger = LoggerFactory.getLogger(ShellyEclaiService.class);

    @Autowired
    private ShellyEclaiDataRepository shellyEclaiDataRepository;

    private static final String API_URL = "https://shelly-106-eu.shelly.cloud/device/status/?id=5432046d02fc&auth_key=MjRiNjYwdWlk571844EF8698B132529C85123EF715DC4F355B8BFD47E4FDB6A2BF53A0B5614E61544A67E40B906D";

    @Scheduled(fixedRate = 1800000) // Exécuter toutes les 30 MINUTES
    public void fetchData() {
        try {
            logger.info("Fetching data from API...");
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(API_URL, HttpMethod.GET, entity, Map.class);

            Map dataMap = (Map) response.getBody().get("data");
            Map deviceStatus = (Map) dataMap.get("device_status");
            Map switchData = (Map) deviceStatus.get("switch:0");
            Map aenergy = (Map) switchData.get("aenergy");
            Map temperature = (Map) switchData.get("temperature");

            // Utilisation de minute_ts pour le timestamp
            Number minuteTsNumber = (Number) aenergy.get("minute_ts");
            Date timestamp = new Date(minuteTsNumber.longValue() * 1000);

            ShellyEclaiData data = new ShellyEclaiData();
            data.setTimestamp(timestamp);
            data.setPower(((Number) switchData.get("apower")).doubleValue());
            data.setVoltage(((Number) switchData.get("voltage")).doubleValue());
            data.setCurrent(((Number) switchData.get("current")).doubleValue());
            data.setTotalEnergy(((Number) aenergy.get("total")).doubleValue());
            data.setTemperatureC(((Number) temperature.get("tC")).doubleValue());
            data.setTemperatureF(((Number) temperature.get("tF")).doubleValue());

            shellyEclaiDataRepository.save(data);
            logger.info("Data saved: " + data);
        } catch (Exception e) {
            logger.error("Error fetching data from API", e);
        }
    }
}
