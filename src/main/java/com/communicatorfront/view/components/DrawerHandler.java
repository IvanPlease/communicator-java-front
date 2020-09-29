package com.communicatorfront.view.components;

import com.communicatorfront.domain.*;
import com.communicatorfront.service.ConvService;
import com.communicatorfront.service.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import lombok.RequiredArgsConstructor;

import java.util.*;

import static java.util.stream.Collectors.toList;

@org.springframework.stereotype.Component
@RequiredArgsConstructor
public class DrawerHandler extends AppLayout {

    private final ContentHandler contentHandler;
    private final UserService userService;
    private ConvService convService;
    private User currentUser;
    private static final String URL = "http://localhost:8085/logout";

    public void init(UserSession userSession) throws Exception {
        this.currentUser = userService.checkForUser(new UserDataCheck(userSession.getUser().getFirstname(),userSession.getUser().getLastname(),userSession.getUser().getEmail()));
        this.convService = new ConvService(currentUser.getId());
    }

    public HorizontalLayout createdBox(GroupMessage groupMessage, Long userId) throws Exception {
        HorizontalLayout friendsBox = new HorizontalLayout();
        Div unReadInfo = new Div();
        Image profilePicture = new Image();
        Span userName = new Span();
        User user = groupMessage.getUsersInConv().stream().filter(u -> !u.getId().equals(userId)).collect(toList()).get(0);
        int counter = convService.countUnreadMessages(groupMessage.getId(), user.getId());

        if(counter > 0) {
            Span unread = new Span();
            unReadInfo.addClassName("unreadMessages");
            unread.setText(String.valueOf(counter));
            unReadInfo.add(unread);
        }

        return getBox(user, friendsBox, unReadInfo, profilePicture, userName);
    }

    public HorizontalLayout createSearchBox(User user){
        HorizontalLayout friendsBox = new HorizontalLayout();
        Div unReadInfo = new Div();
        Image profilePicture = new Image();
        Span userName = new Span();

        return getBox(user, friendsBox, unReadInfo, profilePicture, userName);
    }

    private HorizontalLayout getBox(User user, HorizontalLayout friendsBox, Div unReadInfo, Image profilePicture, Span userName) {
        profilePicture.setSrc(user.getProfilePic().getFilePath());
        profilePicture.setAlt(user.getProfilePic().getFileName());
        profilePicture.addClassName("round-and-round");
        profilePicture.setHeight("44px");
        profilePicture.setWidth("44px");

        userName.setHeightFull();
        userName.setText(user.getFirstname() + " " + user.getLastname());

        friendsBox.setWidthFull();
        friendsBox.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        friendsBox.add(profilePicture);
        friendsBox.add(userName);
        friendsBox.add(unReadInfo);
        return friendsBox;
    }

    public Tab createFriendBox(User user){
        Tab tab = new Tab();
        tab.add(createSearchBox(user));
        tab.getElement().setAttribute("data-user-id", String.valueOf(user.getId()));
        return tab;
    }

    public Tab createConvFriendBox(GroupMessage groupMessage, Long userId) throws Exception {
        Tab tab = new Tab();
        tab.add(createdBox(groupMessage, userId));
        tab.getElement().setAttribute("data-conv-id", String.valueOf(groupMessage.getId()));
        return tab;
    }

    public Component friendTabDrawer() throws Exception {
        LinkedList<GroupMessage> friends = convService.getUsers();
        Tabs tabs = new Tabs(false);
        VerticalLayout friendLayout = new VerticalLayout();
        for(GroupMessage friend:friends){
            tabs.add(createConvFriendBox(friend, currentUser.getId()));
        }
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.setWidthFull();
        tabs.addSelectedChangeListener(e->{
            String convId = e.getSelectedTab().getElement().getAttribute("data-conv-id");
            try {
                e.getSelectedTab().getElement().getChild(0).removeChild(2);
                GroupMessage groupMessage = convService.getConversation(Long.valueOf(convId));
                contentHandler.createConversation(groupMessage, currentUser);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        friendLayout.add(tabs);
        friendLayout.addClassNames("mhv-friend", "overflow-scroll");
        friendLayout.setPadding(false);
        friendLayout.setMargin(false);
        return friendLayout;
    }

    public Component searchTabDrawer(){
        VerticalLayout searchBox = new VerticalLayout();
        TextField textField = new TextField("Wyszkuaj");
        Icon icon = new Icon(VaadinIcon.SEARCH);
        HorizontalLayout prefix = new HorizontalLayout();
        icon.setSize("20px");
        prefix.add(icon);
        prefix.setWidth("25px");
        textField.addClassName("searchInput");
        textField.setPrefixComponent(prefix);
        textField.setClearButtonVisible(true);
        VerticalLayout friend = new VerticalLayout();
        Tabs tabs = new Tabs(false);
        textField.addValueChangeListener(e->{
            tabs.removeAll();
            try {
                LinkedList<User> users = userService.getUsers(e.getValue());
                for(User result:users){
                    if(!result.getId().equals(currentUser.getId())){
                        tabs.add(createFriendBox(result));
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.setWidthFull();
        tabs.addSelectedChangeListener(e->{
            try {
                GroupMessage createdConv = convService.createConversation(currentUser.getId(), Long.parseLong(e.getSelectedTab().getElement().getAttribute("data-user-id")));
                GroupMessage newConv = convService.getConversation(createdConv.getId());
                contentHandler.createConversation(newConv, currentUser);
                tabs.setSelectedIndex(0);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        friend.add(tabs);
        friend.setPadding(false);
        friend.setMargin(false);
        friend.addClassNames("overflow-scroll", "mhv-search");

        searchBox.add(textField);
        searchBox.add(friend);
        searchBox.setAlignItems(FlexComponent.Alignment.CENTER);
        searchBox.setVisible(false);
        searchBox.setPadding(false);
        searchBox.setMargin(false);

        return searchBox;
    }

    public Component createDrawer() throws Exception {
        LinkedList<VaadinIcon> optionIcons = new LinkedList<>();
        optionIcons.add(VaadinIcon.USERS);
        optionIcons.add(VaadinIcon.SEARCH_PLUS);
        optionIcons.add(VaadinIcon.SIGN_OUT);
        Map<Tab, Component> tabsToPages = new HashMap<>();
        VerticalLayout verticalLayout = new VerticalLayout();
        VerticalLayout logoLayout = new VerticalLayout();
        VerticalLayout friendLayout = (VerticalLayout) friendTabDrawer();
        VerticalLayout searchLayout;

        Span appName = new Span();
        logoLayout.add(appName);
        logoLayout.addClassName("logo-span");
        logoLayout.setWidthFull();
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        Tabs optionTabs = new Tabs();
        searchLayout = (VerticalLayout) searchTabDrawer();
        for(VaadinIcon icon:optionIcons){
            Tab inTab = new Tab(new Icon(icon));
            inTab.addClassName("mw-50p");
            if(icon == VaadinIcon.USERS){
                tabsToPages.put(inTab, friendLayout);
            }else if(icon == VaadinIcon.SEARCH_PLUS){
                tabsToPages.put(inTab, searchLayout);
            }
            optionTabs.add(inTab);
        }
        optionTabs.setHeight("50px");
        optionTabs.setWidthFull();
        optionTabs.setFlexGrowForEnclosedTabs(1);
        optionTabs.addSelectedChangeListener(e -> {
            if(e.getSelectedTab().getElement().getChild(0).getAttribute("icon").equals("vaadin:sign-out")){
                UI.getCurrent().getPage().setLocation(URL);
            }else{
                tabsToPages.values().forEach(p -> p.setVisible(false));
                tabsToPages.get(optionTabs.getSelectedTab()).setVisible(true);
                try {
                    createDrawer();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        verticalLayout.setPadding(false);
        verticalLayout.add(logoLayout);
        verticalLayout.add(optionTabs);
        verticalLayout.add(friendLayout);
        verticalLayout.add(searchLayout);

        return verticalLayout;
    }
}
