package com.aevasquez.msvc.clients.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record ClientRequest(
        UUID clientId,

        @NotBlank(message = "El Nombre es Requerido")
        @Size(max = 90, message = "Longitud Máxima es de 90 Carácteres")
        String name,

        @Email(message = "El Email es Requerido")
        @Size(max = 150, message = "Longitud Máxima es de 150 Carácteres")
        String email,

        @NotBlank(message = "El Teléfono es Requerido")
        @Size(max = 90, message = "La Longitud Máxima es de 90 Carácteres")
        String phone,

        @NotNull(message = "La Oferta es Requerido")
        Boolean offers,

        @NotNull(message = "El Estatus es Requerido")
        Boolean status,

        @NotNull(message = "El tipo de Cliente es Requerido")
        ClientType clientType,

        @NotBlank
        String password,

        @Valid
        ClientNaturalRequest clientNatural,

        @Valid
        ClientLegalRequest clientLegal
) {

    public record ClientNaturalRequest(

            @NotBlank(message = "El Primer Nombre es Requerido")
            @NotNull(message = "El Primer Nombre No Puede ser Nulo")
            @Size(max = 15, message = "El Primer Nombre no puede medir más de 15 Carácteres")
            String firstName,

            @NotBlank(message = "El Segundo Nombre es Requerido")
            @Size(max = 15, message = "El Segundo Nombre no puede medir más de 15 Carácteres")
            String secondName,

            @Size(max = 15, message = "El Tercer Nombre no puede medir más de 15 Carácteres")
            String thirdName,

            @NotBlank(message = "El Primer Apellido es Requerido")
            @Size(max = 15, message = "El Primer Apellido no puede medir más de 15 Carácteres")
            String firstLastname,

            @Size(max = 15, message = "El Segundo Apellido no puede medir más de 15 Carácteres")
            String secondLastname,

            @NotNull(message = "La fecha de nacimiento es Requerida")
            @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
            LocalDate birthdate
    ){}

    public record ClientLegalRequest(
            @NotBlank(message = "La Razón Social es Requerida")
            @Size(max = 100, message = "La Razón Social no Puede Medir más de 15 Carácteres")
            String legalName,

            @NotBlank(message = "El Nombre del Comercio es Requerido")
            @Size(max = 100, message = "El Nombre del Comercio no Puede Medir más de 15 Carácteres")
            String commercialName,

            @NotBlank(message = "El NIT es Requerido")
            @Size(max = 15, message = "El NIT del Comercio no Puede Medir más de 15 Carácteres")
            String taxId,

            @NotNull(message = "La Fecha de Constitución es Requerida")
            @PastOrPresent(message = "La Fecha de Constitución no Puede ser Futura")
            LocalDate incorporationDate,

            @NotNull(message = "El representante legal es obligatorio")
            UUID legalRepresentative
    ){}
}
