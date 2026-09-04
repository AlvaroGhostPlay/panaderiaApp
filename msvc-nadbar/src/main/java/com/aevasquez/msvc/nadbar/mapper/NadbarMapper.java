package com.aevasquez.msvc.nadbar.mapper;

import com.aevasquez.msvc.nadbar.dto.NadbarResponseDto;
import com.aevasquez.msvc.nadbar.model.Nadbar;
import org.springframework.stereotype.Service;

@Service
public class NadbarMapper {

    public NadbarResponseDto createNadbarDto(Nadbar nadbar){
        return new NadbarResponseDto(
                nadbar.getIdNadbar(),
                nadbar.getTypeNadbar().getTypeNadbar(),
                nadbar.getPath(),
                nadbar.getTitle()
        );
    }
}
