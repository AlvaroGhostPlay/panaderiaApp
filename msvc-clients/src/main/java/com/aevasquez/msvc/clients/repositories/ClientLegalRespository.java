package com.aevasquez.msvc.clients.repositories;

import com.aevasquez.msvc.clients.model.ClientLegal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientLegalRespository extends JpaRepository<ClientLegal, UUID> {
}
