package com.thoughts.service;

import com.thoughts.dto.MessageDto;
import com.thoughts.mapper.MessageMapper;
import com.thoughts.model.Message;
import com.thoughts.model.User;
import com.thoughts.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public List<Message> findAll(String tag) {
        if (tag != null && !tag.isEmpty()) {
            return messageRepository.findByTag(tag);
        }
        return messageRepository.findAll();
    }

    public Message create(MessageDto messageDto, User user) {
        Message message = messageMapper.map(messageDto);
        message.setAuthor(user);

        return Optional.of(message)
                .map(messageRepository::saveAndFlush)
                .orElseThrow();
    }
}
