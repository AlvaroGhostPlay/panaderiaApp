package com.aevasquez.msvc.clients.dto;

import java.time.LocalDate;
import java.util.UUID;

public record ClientResponseDto(
        UUID clientId,
        String name,
        String email,
        String phone,
        Boolean offers,
        Boolean status,
        String clientType,
        ClienteNatural clienteNatural,
        ClienteLegal clienteLegal
        ) {

    public record ClienteNatural(
            String firstName,
            String secondName,
            String thirdName,
            String firstLastname,
            String secondLastname,
            LocalDate birthdate
    ){}

    public record ClienteLegal(
            String legalName,
            String commercialName,
            String taxId,
            LocalDate incorporationDate,
            UUID legalRepresentative
    ){}
}
