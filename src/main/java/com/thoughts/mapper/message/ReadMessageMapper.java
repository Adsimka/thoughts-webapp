package com.thoughts.mapper.message;

import com.thoughts.dto.message.ReadMessageDto;
import com.thoughts.mapper.Mapper;
import com.thoughts.model.Message;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReadMessageMapper implements Mapper<Message, ReadMessageDto> {

    @Override
    public ReadMessageDto map(Message object) {
        return ReadMessageDto.builder()
                .id(object.getId())
                .text(object.getText())
                .tag(object.getTag())
                .author(object.getAuthor())
                .image(Optional.ofNullable(object.getImage())
                        .filter(image -> !image.isEmpty())
                        .orElse(null))
                .build();
    }
}
