package com.aevasquez.msvc.nadbar.services;

import com.aevasquez.msvc.nadbar.dto.NadbarResponseDto;
import com.aevasquez.msvc.nadbar.model.Nadbar;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NadbarService {
    NadbarResponseDto getNadbarById(UUID id);
    List<NadbarResponseDto> getNadbarByType(String type);
    Optional<Nadbar> createNadbar(Nadbar nadbarRequest);
    Optional<Nadbar> updateNadbarById(Nadbar nadbarRequest, UUID id);
    Optional<Nadbar> deleteNadbarById(UUID id);
}
