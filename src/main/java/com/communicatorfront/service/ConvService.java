package com.communicatorfront.service;

import com.communicatorfront.domain.GroupMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import org.springframework.stereotype.Component;

import static com.communicatorfront.service.UrlReader.readUrl;
import java.util.*;

@Getter
@Component
public class ConvService {
    private final LinkedList<GroupMessage> users;
    private static final String hostUrl = "http://localhost:8083/v1/conv";

    public ConvService(Long id) throws Exception {
        this.users = getConversations(id);
    }

    public ConvService(){
        this.users = new LinkedList<>();
    }

    private LinkedList<GroupMessage> getConversations(Long id) throws Exception {
        LinkedList<GroupMessage> users = new LinkedList<>();
        String url = hostUrl + "/user/" + id;
        String json = readUrl(url, "GET", "");
        Gson gson = new Gson();
        List<Long> convsId = gson.fromJson(json, new TypeToken<ArrayList<Long>>(){}.getType());
        convsId.forEach(c -> {
            try {
                users.add(getConversation(c));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return users;
    }

    public int countUnreadMessages(Long convId, Long userId) throws Exception{
        String url = hostUrl + "/" + convId + "/unread/" + userId;
        return Integer.parseInt(readUrl(url, "GET", ""));
    }

    public GroupMessage createConversation(Long idA, Long idB) throws Exception {
        String jsonArray = "{\"usersInConv\": [{ \"id\": " + idA + "},  { \"id\": " + idB + " }] }";
        String json = readUrl(hostUrl, "POST", jsonArray);
        Gson gson = new Gson();
        return gson.fromJson(json, GroupMessage.class);
    }

    public GroupMessage getConversation(Long convId) throws Exception {
        GroupMessage users;
        String url = hostUrl + "/" + convId;
        String json = readUrl(url, "GET", "");
        Gson gson = new Gson();
        users = gson.fromJson(json, GroupMessage.class);
        return users;
    }
}
