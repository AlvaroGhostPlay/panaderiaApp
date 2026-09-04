package com.aevasquez.msvc.clients.services;

import com.aevasquez.msvc.clients.dto.*;
import com.aevasquez.msvc.clients.exceptions.DataIntegrityViolationException;
import com.aevasquez.msvc.clients.exceptions.NotFoundException;
import com.aevasquez.msvc.clients.mapper.ClientMapper;
import com.aevasquez.msvc.clients.model.*;
import com.aevasquez.msvc.clients.model.ClientType;
import com.aevasquez.msvc.clients.repositories.ClientLegalRespository;
import com.aevasquez.msvc.clients.repositories.ClientNaturalRepository;
import com.aevasquez.msvc.clients.repositories.ClientRepository;
import com.aevasquez.msvc.clients.repositories.ClientTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.aevasquez.msvc.clients.model.Client;
import com.aevasquez.msvc.clients.model.User;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ClientServiceImpl implements ClientService{

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientNaturalRepository clientNaturalRepository;

    @Autowired
    private ClientLegalRespository clientLegalRepository;

    @Autowired
    private ClientTypeRepository clientTypeRepository;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailServiceImpl emailServiceImpl;

    @Autowired
    private EmailService emailService;

    @Transactional
    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Page<Client> getAllClients(Pageable page) {
        return clientRepository.findAll(page);
    }

    @Transactional
    @Override
    public ClientResponseDto getClientById(UUID id) {
        return clientRepository.findById(id)
                .map(client -> {
                    ClientNatural clientNatural = null;
                    ClientLegal clientLegal = null;
                    if (client.getClientType().getClientType().equals(com.aevasquez.msvc.clients.dto.ClientType.N.name())) {
                        clientNatural = clientNaturalRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException("No se encontro el cliente", HttpStatus.NOT_FOUND.value()));
                    } else {
                        clientLegal = clientLegalRepository.findById(id)
                                .orElseThrow(() ->
                                        new NotFoundException("No se encontro el cliente", HttpStatus.NOT_FOUND.value()));
                    }
                    return clientMapper.createClientResponseNaturalAndLegalResponse(client, clientNatural, clientLegal);
                })
                .orElseThrow(() -> new NotFoundException("No se entontro el cliente", HttpStatus.NOT_FOUND.value()));
    }

    @Transactional
    @Override
    public ClientResponseDto createClient(ClientRequest clientRequest) {
        ClientType clientType = clientTypeRepository.findByClientType(clientRequest.clientType().name())
                .orElseThrow(() -> new NotFoundException("El tipo de cliente no existe", HttpStatus.NOT_FOUND.value()));

        if (clientRepository.findByEmail(clientRequest.email()).isPresent()){
            FieldErrorResponse error = new FieldErrorResponse(
                    "email",
                    "El Email Ya Estiste, Cambielo por otro"
            );
            throw new DataIntegrityViolationException("El correo no está disponible", 409, List.of(error));
        }

        Client client = new Client();
        clientMapper.createClientRequest(client,clientRequest);
        client.setClientType(clientType);
        Client savedClient = clientRepository.saveAndFlush(client);
        UUID llave = savedClient.getClientId();
        ClientNatural clientNatural = new ClientNatural();
        ClientLegal clientLegal = new ClientLegal();

        if (clientRequest.clientType().equals(com.aevasquez.msvc.clients.dto.ClientType.N) && llave != null){
            clientMapper.createClientNaturalRequest(clientNatural, clientRequest.clientNatural());
            clientNatural.setClient(savedClient);
            System.out.println(clientNatural);
            clientNaturalRepository.saveAndFlush(clientNatural);
        } else if (clientRequest.clientType().equals(com.aevasquez.msvc.clients.dto.ClientType.L) && llave != null) {
            clientMapper.createClientNaturalRequest(clientNatural, clientRequest.clientNatural());
            clientNatural.setClient(savedClient);
            System.out.println(clientNatural);
            clientNaturalRepository.saveAndFlush(clientNatural);
        }
        ClientResponseDto clientResponseDto = clientMapper.createClientResponseNaturalAndLegalResponse(savedClient, clientNatural, clientLegal);
        return clientResponseDto;
    }

    @Transactional
    @Override
    public ClientResponseDto updateClientById(ClientRequest clientRequest, UUID id) {

        Client clientDb = clientRepository.findById(id)
                .map(client -> clientMapper.createClientRequest(client, clientRequest))
                .orElseThrow(() -> new NotFoundException("No se encuentra el cliente", HttpStatus.NOT_FOUND.value()));

        ClientType clientType = clientTypeRepository.findByClientType(clientRequest.clientType().name())
                .orElseThrow(() -> new NotFoundException("El tipo de cliente no existe", HttpStatus.NOT_FOUND.value()));

        clientDb.setClientType(clientType);

        ClientNatural clientNaturalDb = null;
        ClientLegal clientLegalDb = null;

        if (clientDb.getClientType().getClientType().equals(com.aevasquez.msvc.clients.dto.ClientType.N.name())){
            clientNaturalDb = clientNaturalRepository.findById(id)
                    .map(clientNatural -> clientMapper.createClientNaturalRequest(clientNatural, clientRequest.clientNatural()))
                    .orElseThrow(() -> new NotFoundException("No se encuentra el cliente Natural", HttpStatus.NOT_FOUND.value()));
            clientNaturalRepository.save(clientNaturalDb);
        } else if (clientDb.getClientType().getClientType().equals(com.aevasquez.msvc.clients.dto.ClientType.L.name())) {
            clientLegalDb = clientLegalRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("No se encuentra el cliente Juridico", HttpStatus.NOT_FOUND.value()));
            clientLegalRepository.save(clientLegalDb);
        }

        clientRepository.save(clientDb);
        return clientMapper.createClientResponseNaturalAndLegalResponse(clientDb, clientNaturalDb, clientLegalDb);
    }

    @Transactional
    @Override
    public ClientResponseDto deleteClientById(UUID id) {

        return clientRepository.findById(id)
                .map(clientdb -> {
                    ClientNatural clientNatural = new ClientNatural();
                    ClientLegal clientLegal = new ClientLegal();

                    if (clientdb.getClientType().getClientType().equals(com.aevasquez.msvc.clients.dto.ClientType.N.name())){
                        clientNatural = (clientNaturalRepository.findById(id)
                                .map(clientNatural1 -> {
                                    clientNaturalRepository.deleteById(id);
                                    return clientNatural1;
                                })
                                .orElseThrow(() -> new NotFoundException("No se encuentra el cliente Natural", HttpStatus.NOT_FOUND.value())));

                    } else if (clientdb.getClientType().getClientType().equals(com.aevasquez.msvc.clients.dto.ClientType.L.name())){
                        clientLegal = (clientLegalRepository.findById(id)
                                .map(clientLegal1 -> {
                                    clientLegalRepository.deleteById(id);
                                    return clientLegal1;
                                })
                                .orElseThrow(() -> new NotFoundException("No se encuentra el cliente Juridico", HttpStatus.NOT_FOUND.value())));
                    }
                    clientRepository.deleteById(id);
                    return clientMapper.createClientResponseNaturalAndLegalResponse(clientdb, clientNatural, clientLegal);
                })
                .orElseThrow(() -> new NotFoundException("No se encuentra el cliente", HttpStatus.NOT_FOUND.value()));
    }
}
