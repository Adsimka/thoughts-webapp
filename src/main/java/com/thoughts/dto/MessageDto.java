package com.thoughts.dto;

import com.thoughts.model.User;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MessageDto {

    String text;

    String tag;

    User author;

    String image;
}
