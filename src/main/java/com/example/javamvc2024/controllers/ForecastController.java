package com.example.javamvc2024.controllers;

import com.example.javamvc2024.models.ForecastModel;
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

        var forecasts = getForecasts();
        var meteoForecastsJson = GetMeteoForecastsJson();
        System.out.println(meteoForecastsJson);

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

    private static ArrayList<ForecastModel> getForecasts() {
        var forecasts = new ArrayList<ForecastModel>();
        var row1 = new ForecastModel("2024-03-12 11:00", 1.0);
        var row2 = new ForecastModel("2024-03-12 12:00", 2.0);
        var row3 = new ForecastModel("2024-03-12 13:00", 3.0);

        forecasts.add(row1);
        forecasts.add(row2);
        forecasts.add(row3);
        return forecasts;
    }
}
