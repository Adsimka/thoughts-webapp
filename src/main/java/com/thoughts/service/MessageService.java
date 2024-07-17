package com.thoughts.service;

import com.thoughts.dto.message.MessageDto;
import com.thoughts.mapper.MessageMapper;
import com.thoughts.model.Message;
import com.thoughts.model.User;
import com.thoughts.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {

    private final MessageRepository messageRepository;
    private final ImageService imageService;
    private final MessageMapper messageMapper;

    private static final String DOT = ".";

    public List<Message> findAll(String tag) {
        if (tag != null && !tag.isEmpty()) {
            return messageRepository.findByTag(tag);
        }
        return messageRepository.findAllWithAuthors();
    }

    @Transactional
    public Message create(MessageDto messageDto,
                          User user) {
        Message message = messageMapper.map(messageDto);
        message.setAuthor(user);

        uploadImage(messageDto.getImage(), message);

        return Optional.of(message)
                .map(messageRepository::saveAndFlush)
                .orElseThrow();
    }

    private void uploadImage(MultipartFile file, Message message) {
        if (file != null && !file.isEmpty()) {
            String uuidImage = UUID.randomUUID().toString();
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";

            if (originalFilename != null) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf(DOT));
            }
//            TODO 04.07.2024 : Fix String concatenation
            String filename = uuidImage + fileExtension;
            message.setImage(filename);

            uploadFile(file, filename);
        }
    }

    @SneakyThrows
    private void uploadFile(MultipartFile file, String filename) {
        if (!file.isEmpty()) {
            imageService.upload(filename, file.getInputStream());
        }
    }
}
