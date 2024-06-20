package com.thoughts.service;

import com.thoughts.model.Message;
import com.thoughts.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    public Message create(Message message) {
        return messageRepository.save(message);
    }
}
