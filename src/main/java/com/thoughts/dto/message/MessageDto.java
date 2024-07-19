package com.thoughts.dto.message;

import com.thoughts.model.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Value
@Builder
public class MessageDto {

    @NotBlank(message = "Please fill the message")
    @Length(max = 2048, message = "Message too long!")
    String text;

    @NotBlank(message = "Please fill the tag")
    @Length(max = 16, message = "Tag too long!")
    String tag;

    User author;

    MultipartFile image;
}
