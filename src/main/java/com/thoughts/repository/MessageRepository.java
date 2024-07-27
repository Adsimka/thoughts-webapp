package com.thoughts.repository;

import com.thoughts.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m JOIN FETCH m.author WHERE m.tag = :tag ORDER BY m.id DESC")
    List<Message> findByTag(String tag);

    @Query("SELECT m FROM Message m JOIN FETCH m.author ORDER BY m.id DESC")
    List<Message> findAllWithAuthors();

    @Query("SELECT m FROM Message m JOIN FETCH m.author WHERE m.author.id = :id ORDER BY m.id DESC")
    List<Message> findAllByAuthorId(Long id);
}
