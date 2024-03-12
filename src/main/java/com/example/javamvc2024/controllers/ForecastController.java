package com.example.javamvc2024.controllers;

import com.example.javamvc2024.models.ForecastModel;
import com.example.javamvc2024.models.Root;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

@Controller
public class ForecastController {

    @GetMapping("/")
    public ModelAndView index() throws IOException {
        ModelAndView modelAndView = new ModelAndView("index");

        var meteoForecastsJson = GetMeteoForecastsJson();
        var forecasts = getForecasts(meteoForecastsJson);

        modelAndView.addObject("forecasts", forecasts);

        return modelAndView;
    }

    public static String GetMeteoForecastsJson() throws IOException {
        URL url = new URL("https://api.meteo.lt/v1/places/vilnius/forecasts/long-term");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        String text = "";
        Scanner scanner = new Scanner(url.openStream());
        while (scanner.hasNext()) {
            text += scanner.nextLine();
        }
        scanner.close();
        return text;
    }

    private static ArrayList<ForecastModel> getForecasts(String json) throws JsonProcessingException {
        Root meteoObj = GetObjectFromJson(json);

        var forecasts = new ArrayList<ForecastModel>();
        for (var item : meteoObj.forecastTimestamps) {
            var row = new ForecastModel(item.forecastTimeUtc, item.airTemperature);
            forecasts.add(row);
        }

        return forecasts;
    }

    private static Root GetObjectFromJson(String json) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Root meteoObj = om.readValue(json, Root.class);
        return meteoObj;
    }
}
