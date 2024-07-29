package com.thoughts.dto.message;

import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Value
@Builder
public class EditMessageDto {

    Long id;

    @Length(max = 2048, message = "Message too long!")
    String text;

    @Length(max = 16, message = "Tag too long!")
    String tag;

    MultipartFile image;
}
