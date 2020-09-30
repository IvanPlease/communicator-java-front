package com.communicatorfront.service;

import org.springframework.stereotype.Component;

import static com.communicatorfront.service.UrlReader.readUrl;

@Component
public class MessageService {

    private static final String hostUrl = "http://localhost:8083/v1/conv/msg";

    public void sendMessage(Long userId, Long convId, String message) throws Exception {
        String jsonArray = "{\"author\": {\"id\": " + userId + "}, \"groupMessage\": {\"id\": " + convId + "}, \"content\": \"" + message + "\"}";
        readUrl(hostUrl, "POST", jsonArray);
    }

    public void changeMessageStatus(Long messageId) throws Exception {
        String url = hostUrl + "/read/" + messageId;
        readUrl(url, "PUT", "");
    }
}
