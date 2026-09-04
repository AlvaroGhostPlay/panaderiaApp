package com.aevasquez.msvc.clients.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "client_legals")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ClientLegal {
    @Id
    @Column(name = "client_id", nullable = false)
    private UUID clientId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(
            name = "client_id",
            nullable = false,
            foreignKey = @jakarta.persistence.ForeignKey(
                    name = "fk_client_legals_clients"
            )
    )
    private Client client;

    @Column(name = "legal_name", nullable = false, length = 150)
    private String legalName;

    @Column(name = "commercial_name", length = 150)
    private String commercialName;

    @Column(name = "tax_id", nullable = false, unique = true, length = 30)
    private String taxId;

    @Column(name = "incorporation_date")
    private LocalDate incorporationDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_legal_repre",
            foreignKey = @jakarta.persistence.ForeignKey(
                    name = "fk_client_legals_clients2"
            )
    )
    private Client legalRepresentative;

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getCommercialName() {
        return commercialName;
    }

    public void setCommercialName(String commercialName) {
        this.commercialName = commercialName;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public LocalDate getIncorporationDate() {
        return incorporationDate;
    }

    public void setIncorporationDate(LocalDate incorporationDate) {
        this.incorporationDate = incorporationDate;
    }

    public Client getLegalRepresentative() {
        return legalRepresentative;
    }

    public void setLegalRepresentative(Client legalRepresentative) {
        this.legalRepresentative = legalRepresentative;
    }
}
