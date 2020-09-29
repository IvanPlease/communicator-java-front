package com.communicatorfront.view;

import com.communicatorfront.domain.User;
import com.communicatorfront.domain.UserDataCheck;
import com.communicatorfront.domain.UserSession;
import com.communicatorfront.service.NotificationService;
import com.communicatorfront.service.UserService;
import com.communicatorfront.view.components.DrawerHandler;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.PostConstruct;

@CssImport("./styles/style.css")
@CssImport(value = "./styles/text-field.css", themeFor = "vaadin-text-field")
@Route("")
@Theme(value = Material.class, variant = Material.DARK)
@PageTitle("Login")
@Slf4j
@RequiredArgsConstructor
public class MainView extends AppLayout {

    private final UserSession userSession;
    private final UserService userService;
    private final DrawerHandler drawerHandler;

    @PostConstruct
    public void init() throws Exception {
        userService.createUser(userSession.getUser());
        drawerHandler.init(userSession);
        addToDrawer(drawerHandler.createDrawer());
        //ApiService apiService = new ApiService();
        //Notification.show("Pogoda: " + apiService.getWeatherInfo(), 5000, Notification.Position.BOTTOM_START);
        //Notification.show("Losowy cytat: " + apiService.getRandomQuotes(), 5000, Notification.Position.BOTTOM_START);
    }
}