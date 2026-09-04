package com.aevasquez.msvc.clients.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "client_types", schema = "storedb")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ClientType {

    @Id
    @Column(name = "client_type_id", nullable = false)
    private Integer clientTypeId;

    @Column(name = "client_type", nullable = false, unique = true, length = 20)
    private String clientType;

    public Integer getClientTypeId() {
        return clientTypeId;
    }

    public void setClientTypeId(Integer clientTypeId) {
        this.clientTypeId = clientTypeId;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
}