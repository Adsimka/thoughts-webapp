package com.thoughts.service;

import com.thoughts.dto.message.CreateMessageDto;
import com.thoughts.dto.message.ReadMessageDto;
import com.thoughts.mapper.message.CreateMessageMapper;
import com.thoughts.mapper.message.ReadMessageMapper;
import com.thoughts.model.Message;
import com.thoughts.model.User;
import com.thoughts.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MessageService {

    private static final String DOT = ".";

    private final MessageRepository messageRepository;
    private final ImageService imageService;

    private final CreateMessageMapper createMessageMapper;
    private final ReadMessageMapper readMessageMapper;


    public List<ReadMessageDto> findAll(String tag) {
        if (tag != null && !tag.isEmpty()) {
            return messageRepository.findByTag(tag).stream()
                    .map(readMessageMapper::map)
                    .toList();
        }
        return messageRepository.findAllWithAuthors().stream()
                .map(readMessageMapper::map)
                .toList();
    }

    @Transactional
    public Message create(CreateMessageDto createMessageDto,
                          User user) {
        Message message = createMessageMapper.map(createMessageDto);
        message.setAuthor(user);

        uploadFile(createMessageDto.getImage(), message);

        return Optional.of(message)
                .map(messageRepository::saveAndFlush)
                .orElseThrow();
    }

    private void uploadFile(MultipartFile file, Message message) {
        if (file != null && !file.isEmpty()) {
            String uuidImage = UUID.randomUUID().toString();
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";

            if (originalFilename != null) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf(DOT));
            }
            String filename = uuidImage + fileExtension;
            message.setImage(filename);

            uploadFile(file, filename);
        }
    }

    @SneakyThrows
    private void uploadFile(MultipartFile file, String filename) {
        if (!file.isEmpty()) {
            imageService.upload(filename, file.getInputStream());
            log.info("File upload {}", file.getOriginalFilename());
        }
    }
}
