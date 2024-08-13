package com.thoughts.repository;

import com.thoughts.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m JOIN FETCH m.author WHERE m.tag = :tag ORDER BY m.id DESC")
    Page<Message> findByTag(String tag, Pageable pageable);

    @Query("SELECT m FROM Message m JOIN FETCH m.author ORDER BY m.id DESC")
    Page<Message> findAllWithAuthors(Pageable pageable);

    @Query("SELECT m FROM Message m WHERE m.author.id = :id ORDER BY m.id DESC")
    List<Message> findAllByAuthorId(Long id);

    @Query("SELECT m FROM Message m WHERE m.id = :id")
    Optional<Message> findById(Long id);
}
