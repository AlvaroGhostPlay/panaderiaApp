package com.aevasquez.msvc.nadbar.services;

import com.aevasquez.msvc.nadbar.dto.ListConentResponseDto;
import com.aevasquez.msvc.nadbar.model.Content;

import java.util.UUID;

public interface ContentService {
    ListConentResponseDto  getContentsByTypeContent(String contentType);
    Content getContentById(UUID id);
}
