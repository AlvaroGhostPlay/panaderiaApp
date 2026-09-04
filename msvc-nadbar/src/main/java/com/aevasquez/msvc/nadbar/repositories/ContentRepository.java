package com.aevasquez.msvc.nadbar.repositories;

import com.aevasquez.msvc.nadbar.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContentRepository extends JpaRepository<Content, UUID> {

    List<Content> findContentByContentType_ContentType(String contentType);
}
