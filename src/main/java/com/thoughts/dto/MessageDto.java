package com.thoughts.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MessageDto {

    String text;

    String tag;
}
