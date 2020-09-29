package com.communicatorfront.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class UserDataCheck {
    String firstname;
    String lastname;
    String email;
}
