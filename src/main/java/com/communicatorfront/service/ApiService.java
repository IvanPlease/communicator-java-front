package com.communicatorfront.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("DuplicatedCode")
@Component
public class ApiService {
    private final Gson gson = new Gson();

    public static String readUrl(String urlString, String method, String json, boolean voided) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-type", "application/json");
            conn.setRequestProperty("Accept", "*/*");
            if (voided) {
                conn.setRequestProperty("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com");
            } else {
                conn.setRequestProperty("x-rapidapi-host", "quotes25.p.rapidapi.com");
                conn.setRequestProperty("useQueryString", String.valueOf(true));
            }
            conn.setRequestProperty("x-rapidapi-key", "a7012ccf27mshb9073576472dceep130e61jsn7df19140ed65");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            if (!json.equals("")) {
                OutputStream os = conn.getOutputStream();
                os.write(json.getBytes(StandardCharsets.UTF_8));
                os.close();
            }
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    public String getWeatherInfo() throws Exception {
        String url = "https://community-open-weather-map.p.rapidapi.com/weather?id=2172797&units=metric&q=Gdańsk,pl&lang=pl";
        String json = readUrl(url, "GET", "", true);
        JsonObject object = gson.fromJson(json, JsonObject.class);
        return object.getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("main").getAsString();
    }

    public String getRandomQuotes() throws Exception {
        String url = "https://quotes25.p.rapidapi.com/random";
        String json = readUrl(url, "GET", "", false);
        JsonObject object = gson.fromJson(json, JsonObject.class);
        return object.getAsJsonObject().get("message").getAsString();
    }
}
