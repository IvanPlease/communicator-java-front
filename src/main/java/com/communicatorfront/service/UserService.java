package com.communicatorfront.service;

import com.communicatorfront.domain.User;
import com.communicatorfront.domain.UserDataCheck;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.regex.Pattern;

import static com.communicatorfront.service.UrlReader.readUrl;

@Getter
@Component
public class UserService {
    private static final String hostUrl = "http://localhost:8083/v1/users";
    private final LinkedList<User> users = new LinkedList<>();

    public LinkedList<User> getUsers(String pattern) throws Exception {
        LinkedList<User> users;
        Pattern mailPattern = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$");
        Pattern userPatternOne = Pattern.compile("^\\b[a-zA-Z]+\\b\\s\\b[a-zA-Z]+\\b$");
        Pattern userPatternTwo = Pattern.compile("\\b[a-zA-Z]+\\b$");
        long searchType = 3L;
        if (mailPattern.matcher(pattern).matches()) {
            searchType = 2L;
        } else if (userPatternOne.matcher(pattern).matches()) {
            searchType = 1L;
        } else if (userPatternTwo.matcher(pattern).matches()) {
            searchType = 0L;
        }
        String url = hostUrl + "/pattern" + "?searchType=" + searchType + "&searchValue=" + pattern;
        String json = readUrl(url, "GET", "");
        Gson gson = new Gson();
        users = gson.fromJson(json, new TypeToken<LinkedList<User>>(){}.getType());
        return users;
    }

    public void createUser(User user) throws Exception {
        Gson gson = new Gson();
        String jsonArray = gson.toJson(user);
        String json = readUrl(hostUrl, "POST", jsonArray);
        gson.fromJson(json, User.class);
    }

    public User checkForUser(UserDataCheck userDataCheck) throws Exception {
        Gson gson = new Gson();
        String json = readUrl(hostUrl + "/googleauth?firstname=" + userDataCheck.getFirstname() + "&lastname=" + userDataCheck.getLastname() + "&email=" + userDataCheck.getEmail(), "GET", "");
        return gson.fromJson(json, User.class);
    }

}
