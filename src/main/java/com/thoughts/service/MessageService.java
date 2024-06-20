package com.thoughts.service;

import com.thoughts.dto.MessageDto;
import com.thoughts.mapper.MessageMapper;
import com.thoughts.model.Message;
import com.thoughts.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    public Message create(MessageDto messageDto) {
        return Optional.of(messageDto)
                .map(messageMapper::map)
                .map(messageRepository::saveAndFlush)
                .orElseThrow();
    }
}
