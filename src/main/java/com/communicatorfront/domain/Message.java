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
public class Message {
    private Long id;
    private User author;
    private String content;
    private List<Attachments> attachmentsList;
    private boolean read;
}
