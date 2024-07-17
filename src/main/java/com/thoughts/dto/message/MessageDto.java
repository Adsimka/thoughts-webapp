package com.thoughts.dto.message;

import com.thoughts.model.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

@Value
@Builder
public class MessageDto {

    @NotNull
    @NotEmpty
    String text;

    @NotNull
    @NotEmpty
    String tag;

    @NotNull
    @NotEmpty
    User author;

    MultipartFile image;
}
