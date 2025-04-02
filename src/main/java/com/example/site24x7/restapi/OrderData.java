package com.example.site24x7.restapi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OrderData {
    public static JSONArray orderData(JSONArray jsonArray, String interval) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<JSONObject> sortedList = jsonArray.toList().stream()
                .map(obj -> new JSONObject((java.util.Map<?, ?>) obj))
                .sorted(Comparator.comparing(obj -> {
                    if (obj.has("time_slot") || obj.has("hour_slot")) {
                        String dateTimeStr = obj.has("time_slot") ? obj.getString("time_slot") : obj.getString("hour_slot");
                        try {
                            return LocalDateTime.parse(dateTimeStr, dateTimeFormatter);
                        } catch (DateTimeParseException e) {
                            System.err.println("Invalid DateTime format: " + dateTimeStr);
                            return LocalDateTime.MIN;
                        }
                    } else if (obj.has("date")) {
                        String dateStr = obj.getString("date");
                        try {
                            return LocalDate.parse(dateStr, dateFormatter).atTime(23, 59, 59); // Adding default time
                        } catch (DateTimeParseException e) {
                            System.err.println("Invalid Date format: " + dateStr);
                            return LocalDateTime.MIN;
                        }
                    }
                    return LocalDateTime.MIN; // Default value for missing dates
                }, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        return new JSONArray(sortedList);
    }
}
