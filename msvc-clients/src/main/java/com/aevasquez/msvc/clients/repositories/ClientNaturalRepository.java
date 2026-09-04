package com.aevasquez.msvc.clients.repositories;

import com.aevasquez.msvc.clients.model.ClientNatural;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientNaturalRepository extends JpaRepository<ClientNatural, UUID> {
}
