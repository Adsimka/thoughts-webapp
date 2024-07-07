package com.thoughts.mapper;

import com.thoughts.dto.MessageDto;
import com.thoughts.model.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Component
public class MessageMapper implements Mapper<MessageDto, Message> {

    @Override
    public Message map(MessageDto object) {
        return Message.builder()
                .text(object.getText())
                .tag(object.getTag())
                .image(Optional.ofNullable(object.getImage())
                        .filter(image -> !image.isEmpty())
                        .map(MultipartFile::getOriginalFilename)
                        .orElse(null))
                .build();
    }
}
