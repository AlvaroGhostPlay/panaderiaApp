package com.aevasquez.msvc.nadbar.repositories;

import com.aevasquez.msvc.nadbar.model.Nadbar;
import com.aevasquez.msvc.nadbar.model.TypeNadbar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NadbarRepository extends JpaRepository<Nadbar, UUID> {
    List<Nadbar> findByTypeNadbar_TypeNadbar(String typeNadbar);
}
