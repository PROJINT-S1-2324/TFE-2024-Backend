package prise.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import prise.example.demo.model.ShellyData;
import prise.example.demo.repository.ShellyDataRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;


@RestController
public class ShellyDataController {

    @Autowired
    private ShellyDataRepository shellyDataRepository;

    /*
        @CrossOrigin(origins = "http://localhost:3000")
        @RequestMapping("/data/day")
        @GetMapping()
        public List<ShellyData> getDataByDay(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
            // Définir les limites de la journée (de minuit à 23h59)
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date start = calendar.getTime();

            calendar.add(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.MILLISECOND, -1);
            Date end = calendar.getTime();

            return shellyDataRepository.findByTimestampBetween(start, end);
        }

        @GetMapping("/data/week")
        public List<ShellyData> getDataByWeek(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
            // Définir les limites de la semaine (du lundi 00:00 au dimanche 23:59)
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date start = calendar.getTime();

            calendar.add(Calendar.WEEK_OF_YEAR, 1);
            calendar.add(Calendar.MILLISECOND, -1);
            Date end = calendar.getTime();

            return shellyDataRepository.findByTimestampBetween(start, end);
        }

        @GetMapping("/data/month")
        public List<ShellyData> getDataByMonth(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM") Date date) {
            // Définir les limites du mois (du premier jour 00:00 au dernier jour 23:59)
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date start = calendar.getTime();

            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.MILLISECOND, -1);
            Date end = calendar.getTime();

            return shellyDataRepository.findByTimestampBetween(start, end);
        }
        */
//http://20.123.48.27:8080/data/day?date=2024-06-2
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/data/energy/hourly")
    public List<Map<String, Object>> getHourlyEnergy(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        Map<String, Double> hourlyEnergy = new LinkedHashMap<>();
        List<Map<String, Object>> resultTable = new ArrayList<>();

        // Initialiser le calendrier pour la date spécifiée à minuit
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date start = calendar.getTime();

        // Définir la fin de la journée à 23:59:59.999
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        Date endOfDay = calendar.getTime();

        // Log de debug
        System.out.println("Start: " + start);
        System.out.println("End of Day: " + endOfDay);

        // Récupérer toutes les données de la journée
        List<ShellyData> dataList = shellyDataRepository.findByTimestampBetween(start, endOfDay);

        if (dataList == null || dataList.isEmpty()) {
            return resultTable;
        }

        // Trier les données par timestamp
        dataList.sort(Comparator.comparing(ShellyData::getTimestamp));

        // Log des données récupérées
        dataList.forEach(data -> System.out.println("Data: " + data.getTimestamp() + ", " + data.getTotalEnergy()));

        // Filtrer les relevés aux heures exactes (xx:59:59)
        List<ShellyData> hourlyDataList = dataList.stream()
                .filter(data -> {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(data.getTimestamp());
                    return cal.get(Calendar.MINUTE) == 59 && cal.get(Calendar.SECOND) == 59;
                })
                .collect(Collectors.toList());

        // Initialiser une variable pour suivre la consommation par heure
        double previousEnergy = hourlyDataList.get(0).getTotalEnergy();

        // Boucler à travers les relevés horaires pour calculer la consommation pour chaque heure
        for (int i = 1; i < hourlyDataList.size(); i++) {
            ShellyData currentData = hourlyDataList.get(i);
            double currentEnergy = currentData.getTotalEnergy();
            double hourlyConsumption = currentEnergy - previousEnergy;

            // Formater l'étiquette de l'heure
            String hourLabel = new SimpleDateFormat("yyyy-MM-dd HH:00:00").format(currentData.getTimestamp());

            // Ajouter la consommation horaire au résultat
            hourlyEnergy.put(hourLabel, hourlyConsumption);

            // Ajouter au tableau de résultat
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("hour", hourLabel);
            row.put("consumption", hourlyConsumption);
            resultTable.add(row);

            // Mettre à jour l'énergie précédente
            previousEnergy = currentEnergy;

            // Log de debug pour chaque heure
            System.out.println("Hour: " + hourLabel + " - Consumption: " + hourlyConsumption);
        }

        return resultTable;
    }


}

