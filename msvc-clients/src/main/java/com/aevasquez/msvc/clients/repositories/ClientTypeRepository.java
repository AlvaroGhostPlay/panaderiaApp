package com.aevasquez.msvc.clients.repositories;

import com.aevasquez.msvc.clients.model.ClientType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientTypeRepository extends JpaRepository<ClientType, Integer> {
    Optional<ClientType> findByClientType(String clientType);
}
