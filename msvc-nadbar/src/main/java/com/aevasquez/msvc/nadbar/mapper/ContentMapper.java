package com.aevasquez.msvc.nadbar.mapper;

import com.aevasquez.msvc.nadbar.dto.ContentResponseDto;
import com.aevasquez.msvc.nadbar.model.Content;
import org.springframework.stereotype.Service;

@Service
public class ContentMapper {

    public ContentResponseDto createContent(Content content){
        return new ContentResponseDto(
                content.getContentId(),
                content.getIdentifier(),
                content.getContent(),
                content.getType()
        );

    }
}
