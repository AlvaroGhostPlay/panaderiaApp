package com.aevasquez.msvc.nadbar.services;

import com.aevasquez.msvc.nadbar.dto.ContentResponseDto;
import com.aevasquez.msvc.nadbar.dto.ListConentResponseDto;
import com.aevasquez.msvc.nadbar.mapper.ContentMapper;
import com.aevasquez.msvc.nadbar.model.Content;
import com.aevasquez.msvc.nadbar.repositories.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ContentServiceImpl implements ContentService{

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private ContentMapper contentMapper;

    @Transactional
    @Override
    public ListConentResponseDto getContentsByTypeContent(String contentType) {
        List<ContentResponseDto> listConentResponseDto =  contentRepository.findContentByContentType_ContentType(contentType)
                .stream()
                .map(contentMapper::createContent)
                .toList();
        if (listConentResponseDto!=null){
            return new ListConentResponseDto(
                    contentType,
                    listConentResponseDto
            );
        }
        return null;
    }

    @Override
    public Content getContentById(UUID id) {
        return null;
    }
}
