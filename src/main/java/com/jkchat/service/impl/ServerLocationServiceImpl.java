package com.jkchat.service.impl;

import com.google.gson.Gson;
import com.jkchat.models.ServerLocation;
import com.jkchat.service.ServerLocationService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

@Service
public class ServerLocationServiceImpl implements ServerLocationService {

    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(
                    new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
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

    @Override
    public ServerLocation getLocation(String ipAddress) {

        ServerLocation serverLocation = null;
        String json = null;
        try {
            json = readUrl("http://ip-api.com/json/" + ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        serverLocation = gson.fromJson(json, ServerLocation.class);
        return serverLocation;

    }
}
