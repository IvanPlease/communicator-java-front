package com.communicatorfront.domain;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private boolean banned;
    private int status;
    private Attachments profilePic;
}