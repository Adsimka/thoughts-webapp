package com.thoughts.dto.message;

import com.thoughts.model.User;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReadMessageDto {

    Long id;

    String text;

    String tag;

    User author;

    String image;
}
