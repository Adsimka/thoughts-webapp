package com.thoughts.dto;

import com.thoughts.model.User;
import lombok.Builder;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

@Value
@Builder
public class MessageDto {

    String text;

    String tag;

    User author;

    MultipartFile image;
}
