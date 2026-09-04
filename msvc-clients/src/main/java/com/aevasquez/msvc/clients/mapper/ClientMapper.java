package com.aevasquez.msvc.clients.mapper;

import com.aevasquez.msvc.clients.dto.ClientRequest;
import com.aevasquez.msvc.clients.dto.ClientResponseDto;
import com.aevasquez.msvc.clients.dto.ClientType;
import com.aevasquez.msvc.clients.model.Client;
import com.aevasquez.msvc.clients.model.ClientLegal;
import com.aevasquez.msvc.clients.model.ClientNatural;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientMapper {

    public Client createClientRequest(Client client, ClientRequest clientRequest){
        client.setCreatedAt(LocalDateTime.now());
        client.setEmail(clientRequest.email());
        client.setOffers(clientRequest.offers());
        client.setStatus(clientRequest.status());
        client.setPhone(clientRequest.phone());
        client.setUpdatedAt(LocalDateTime.now());
        client.setName(clientRequest.name());
        return client;
    }

    public ClientNatural createClientNaturalRequest(ClientNatural clientNatural, ClientRequest.ClientNaturalRequest clientRequest){
        clientNatural.setFirstName(clientRequest.firstName());
        clientNatural.setSecondName(clientRequest.secondName());
        clientNatural.setThirdName(clientRequest.thirdName());
        clientNatural.setFirstLastname(clientRequest.firstLastname());
        clientNatural.setSecondLastname(clientRequest.secondLastname());
        clientNatural.setBirthdate(clientRequest.birthdate());
        return clientNatural;
    }

    public ClientResponseDto createClientResponseNaturalAndLegalResponse(Client client, ClientNatural clientNatural, ClientLegal clientLegal){
        com.aevasquez.msvc.clients.dto.ClientResponseDto.ClienteNatural clienteNatural = null;
        com.aevasquez.msvc.clients.dto.ClientResponseDto.ClienteLegal clienteLegal = null;
        System.out.println(client.getClientType().getClientType());
        if (client.getClientType().getClientType().equals(ClientType.N.name())){
            clienteNatural = new com.aevasquez.msvc.clients.dto.ClientResponseDto.ClienteNatural(
                    clientNatural.getFirstName(),
                    clientNatural.getSecondName(),
                    clientNatural.getThirdName(),
                    clientNatural.getFirstLastname(),
                    clientNatural.getSecondLastname(),
                    clientNatural.getBirthdate()
            );
        } else if (client.getClientType().getClientType().equals(ClientType.L.name())){
            clienteLegal = new ClientResponseDto.ClienteLegal(
                    clientLegal.getLegalName(),
                    clientLegal.getCommercialName(),
                    clientLegal.getTaxId(),
                    clientLegal.getIncorporationDate(),
                    clientLegal.getLegalRepresentative().getClientId()
            );
        }

        return new com.aevasquez.msvc.clients.dto.ClientResponseDto(
                        client.getClientId(),
                        client.getName(),
                        client.getEmail(),
                        client.getPhone(),
                        client.getOffers(),
                        client.getStatus(),
                        client.getClientType().getClientType(),
                        clienteNatural,
                        clienteLegal);
    }
}
