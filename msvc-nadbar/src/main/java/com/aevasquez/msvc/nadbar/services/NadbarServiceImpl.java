package com.aevasquez.msvc.nadbar.services;

import com.aevasquez.msvc.nadbar.dto.NadbarResponseDto;
import com.aevasquez.msvc.nadbar.mapper.NadbarMapper;
import com.aevasquez.msvc.nadbar.model.Nadbar;
import com.aevasquez.msvc.nadbar.repositories.NadbarRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NadbarServiceImpl implements NadbarService{

    @Autowired
    private NadbarRepository nadbarRepository;

    @Autowired
    private NadbarMapper nadbarMapper;

    @Override
    public NadbarResponseDto getNadbarById(UUID id) {
        return nadbarRepository.findById(id)
                .map(nadbarMapper::createNadbarDto)
                .orElseThrow(() -> new NotFoundException());
    }

    @Transactional
    @Override
    public List<NadbarResponseDto> getNadbarByType(String type) {
        return nadbarRepository.findByTypeNadbar_TypeNadbar(type)
                .stream()
                .map(nadbarMapper::createNadbarDto)
                .toList();
    }

    @Transactional
    @Override
    public Optional<Nadbar> createNadbar(Nadbar nadbarRequest) {
        return Optional.of(nadbarRepository.save(nadbarRequest));
    }

    @Transactional
    @Override
    public Optional<Nadbar> updateNadbarById(Nadbar nadbarRequest, UUID id) {
        Optional<Nadbar> nadbarDb= nadbarRepository.findById(id);
        if (nadbarDb.isPresent()){
            nadbarDb.get().setPath(nadbarRequest.getPath());
            nadbarDb.get().setTitle(nadbarRequest.getTitle());
        }
        return nadbarDb;
    }

    @Transactional
    @Override
    public Optional<Nadbar> deleteNadbarById(UUID id) {
        Optional<Nadbar> nadbarDb= Optional.of(nadbarRepository.findById(id).orElseThrow(() -> new NotFoundException("jsjdjdj")));
        if (nadbarDb.isPresent()){
            nadbarRepository.deleteById(id);
        }
        return nadbarDb;
    }
}
