package com.communicatorfront.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

@Route("login")
@Theme(value = Material.class, variant = Material.DARK)
@PageTitle("Login")
@CssImport("./styles/style.css")
public class LoginView extends FlexLayout {

    private static final String URL = "http://localhost:8085/oauth2/authorization/google";

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientkey;

    @PostConstruct
    public void initView() {

        // Check that oauth keys are present
        if (clientkey == null || clientkey.isEmpty() || clientkey.length() < 16) {
            Paragraph text = new Paragraph("Could not find OAuth client key in application.properties. "
                    + "Please double-check the key and refer to the README.md file for instructions.");
            text.getStyle().set("padding-top", "100px");
            add(text);

        } else {
            setAlignItems(Alignment.CENTER);
            setSizeFull();
            VerticalLayout layout = new VerticalLayout();
            Div div = new Div();
            Span gplusLoginButton = new Span("Login with Google");
            Image logo = new Image("https://lh3.googleusercontent.com/proxy/4nGl7gcSM4hg3vW0rsgSLDdrmZ8EOud56vM8cprQYxhUrRdV0wMHcCTplaoTgVgOKUjshRtWsxi_XBpqjWGHe8TUdrRMYQpFgZ7E3k7L12WOTYBC3QM", "googleLogo");
            layout.setAlignSelf(Alignment.CENTER);
            div.addClassNames("ma", "login-button");
            div.add(logo);
            div.add(gplusLoginButton);
            layout.add(div);
            layout.addClickListener(e-> UI.getCurrent().getPage().setLocation(URL));
            add(layout);
        }

    }
}