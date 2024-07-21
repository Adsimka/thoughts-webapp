package com.thoughts.mapper.message;

import com.thoughts.dto.message.CreateMessageDto;
import com.thoughts.mapper.Mapper;
import com.thoughts.model.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Component
public class CreateMessageMapper implements Mapper<CreateMessageDto, Message> {

    @Override
    public Message map(CreateMessageDto object) {
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
