package com.communicatorfront.comparator;

import com.communicatorfront.domain.Message;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class CustomComparator implements Comparator<Message> {
    @Override
    public int compare(Message o1, Message o2) {
        return o1.getId().compareTo(o2.getId());
    }
}