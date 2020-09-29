package com.communicatorfront.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupMessage {
    private Long id;
    private List<User> usersInConv;
    private List<Message> messagesInConv;
    private String customName;
    private Attachments groupPicture;
}

