package com.communicatorfront.view.components;

import com.communicatorfront.comparator.CustomComparator;
import com.communicatorfront.domain.GroupMessage;
import com.communicatorfront.domain.Message;
import com.communicatorfront.domain.User;
import com.communicatorfront.service.ConvService;
import com.communicatorfront.service.MessageService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContentHandler {

    private final ConvService convService;
    private final MessageService msgService;

    public void createConversation(GroupMessage groupMessage, User currentUser) throws Exception {

        if (UI.getCurrent().getElement().getChild(0).getChildren().count() > 1) {
            if (UI.getCurrent().getElement().getChild(0).getChild(1).hasAttribute("data-message-box")) {
                UI.getCurrent().getElement().getChild(0).removeChild(1);
            }
        }

        VerticalLayout mainBox = new VerticalLayout();
        VerticalLayout convInfoBox = new VerticalLayout();
        VerticalLayout convMessageBox = new VerticalLayout();
        HorizontalLayout convMessageInput = new HorizontalLayout();
        Span userName = new Span();
        TextField messageInput = new TextField();
        Button messageButton = new Button();
        Icon sendIcon = new Icon(VaadinIcon.PAPERPLANE_O);
        ArrayList<String> nameParts = new ArrayList<>();
        if (groupMessage.getCustomName() == null) {
            groupMessage.getUsersInConv().forEach(u -> {
                if (!u.getId().equals(currentUser.getId())) {
                    nameParts.add(u.getFirstname() + " " + u.getLastname());
                }
            });
        }

        StringBuilder chatName = new StringBuilder();

        for (String part : nameParts) {
            chatName.append(part).append(", ");
        }

        sendIcon.addClassName("centered");
        sendIcon.setSize("25px");

        messageInput.setPlaceholder("Wiadomosc");
        messageInput.addClassName("inputMessageBox");
        messageInput.addKeyPressListener(e -> {
            if (e.getKey().matches("Enter")) {
                sendMessage(groupMessage, currentUser, messageInput);
            }
        });

        messageButton.setIcon(sendIcon);
        messageButton.addClickListener(e -> sendMessage(groupMessage, currentUser, messageInput));

        userName.addClassName("userNameConv");
        userName.setText(chatName.toString());

        convInfoBox.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        convInfoBox.addClassName("convInfoBox");
        convInfoBox.add(userName);

        convMessageBox.addClassNames("messageBox", "overflow-scroll");
        List<Message> messageList = groupMessage.getMessagesInConv();
        messageList.sort(new CustomComparator());
        Collections.reverse(messageList);
        for (Message message : messageList) {
            if (!message.isRead() && !message.getAuthor().getId().equals(currentUser.getId())) {
                msgService.changeMessageStatus(message.getId());
            }
            if (message.getAuthor().getId().equals(currentUser.getId())) {
                convMessageBox.add(createMessageBlop(message, true));
            } else {
                convMessageBox.add(createMessageBlop(message, false));
            }
        }

        convMessageInput.add(messageInput);
        convMessageInput.add(messageButton);
        convMessageInput.addClassNames("inputBoxMessage", "centered");
        convMessageInput.setAlignSelf(FlexComponent.Alignment.CENTER);
        convMessageInput.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        mainBox.add(convInfoBox);
        mainBox.add(convMessageBox);
        mainBox.add(convMessageInput);
        mainBox.setPadding(false);
        mainBox.setMargin(false);
        mainBox.getElement().setAttribute("data-message-box", true);

        UI.getCurrent().getElement().getChild(0).appendChild(mainBox.getElement());

    }

    private void sendMessage(GroupMessage groupMessage, User currentUser, TextField messageInput) {
        try {
            if (!messageInput.getValue().equals("")) {
                msgService.sendMessage(currentUser.getId(), groupMessage.getId(), messageInput.getValue());
                messageInput.setValue("");
                createConversation(convService.getConversation(groupMessage.getId()), currentUser);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public VerticalLayout createMessageBlop(Message message, boolean author) {
        VerticalLayout messageBlop = new VerticalLayout();
        Span content = new Span(message.getContent());
        messageBlop.addClassName("message-blop");
        messageBlop.getElement().setAttribute("data-author", author);
        messageBlop.getElement().setAttribute("data-message-id", String.valueOf(message.getId()));
        messageBlop.add(content);
        return messageBlop;
    }

}
