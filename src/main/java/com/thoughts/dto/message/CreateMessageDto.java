package com.thoughts.dto.message;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMessageDto {

    @NotBlank(message = "Please fill the message")
    @Length(max = 2048, message = "Message too long!")
    String text;

    @NotBlank(message = "Please fill the tag")
    @Length(max = 16, message = "Tag too long!")
    String tag;

    MultipartFile image;
}
