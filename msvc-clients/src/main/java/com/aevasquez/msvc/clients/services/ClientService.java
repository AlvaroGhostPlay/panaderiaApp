package com.aevasquez.msvc.clients.services;

import com.aevasquez.msvc.clients.dto.ClientRequest;
import com.aevasquez.msvc.clients.dto.ClientResponseDto;
import com.aevasquez.msvc.clients.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ClientService {
    List<Client> getAllClients();
    Page<Client> getAllClients(Pageable page);
    ClientResponseDto getClientById(UUID id);
    ClientResponseDto createClient(ClientRequest clientRequest);
    ClientResponseDto updateClientById(ClientRequest clientRequest, UUID id);
    ClientResponseDto deleteClientById(UUID id);
}
