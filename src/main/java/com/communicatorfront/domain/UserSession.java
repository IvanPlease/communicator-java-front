package com.communicatorfront.domain;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Component;

@Component
public class UserSession {

    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticatedPrincipal principal = (OAuth2AuthenticatedPrincipal) authentication.getPrincipal();

        return User.builder()
                .email(principal.getAttribute("email"))
                .firstname(principal.getAttribute("given_name"))
                .lastname(principal.getAttribute("family_name"))
                .profilePic(Attachments.builder().fileName("googleProfilePic").filePath(principal.getAttribute("picture")).build())
                .build();
    }

}
