package com.thoughts.mapper.message;

import com.thoughts.dto.message.EditMessageDto;
import com.thoughts.mapper.Mapper;
import com.thoughts.model.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Component
public class EditMessageMapper implements Mapper<EditMessageDto, Message> {

    @Override
    public Message map(EditMessageDto object) {
        return Message.builder()
                .text(object.getText())
                .tag(object.getTag())
                .image(Optional.ofNullable(object.getImage())
                        .filter(image -> !image.isEmpty())
                        .map(MultipartFile::getOriginalFilename)
                        .orElse(null))
                .build();
    }

    public Message map(EditMessageDto object, Message message) {
        buildMessage(object, message);
        return message;
    }

    private void buildMessage(EditMessageDto object, Message message) {
        if (!object.getText().isEmpty()) {
            message.setText(object.getText());
        }
        if (!object.getTag().isEmpty()) {
            message.setTag(object.getTag());
        }
    }
}
