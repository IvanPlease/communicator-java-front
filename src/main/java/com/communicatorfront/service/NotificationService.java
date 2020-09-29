package com.communicatorfront.service;

import com.communicatorfront.domain.Notification;
import com.communicatorfront.domain.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

import static com.communicatorfront.service.UrlReader.readUrl;

@Component
public class NotificationService {

    private static final String hostUrl = "http://localhost:8083/v1/users/notification";

    public List<Notification> getMessages(Long userId) throws Exception {
        Gson gson = new Gson();
        String json = readUrl(hostUrl + "/" + userId, "GET", "");
        return gson.fromJson(json, new TypeToken<LinkedList<com.communicatorfront.domain.Notification>>(){}.getType());
    }

    public void removeNotification(Long notId) throws Exception {
        String url = hostUrl + "/delete/" + notId;
        readUrl(url, "PUT", "");
    }
}
