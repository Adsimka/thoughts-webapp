package com.thoughts.mapper;

import com.thoughts.dto.MessageDto;
import com.thoughts.model.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper implements Mapper<MessageDto, Message> {

    @Override
    public Message map(MessageDto object) {
        return Message.builder()
                .text(object.getText())
                .tag(object.getTag())
                .image(object.getImage())
                .build();
    }
}
