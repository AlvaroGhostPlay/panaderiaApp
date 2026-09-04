package com.aevasquez.msvc.nadbar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "type_nadbars")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TypeNadbar {

    @Id
    @Column(name = "type_nadbar")
    private String typeNadbar;

    @OneToMany(mappedBy = "typeNadbar", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Nadbar> nadbars;

    public String getTypeNadbar() {
        return typeNadbar;
    }

    public void setTypeNadbar(String typeNadbar) {
        this.typeNadbar = typeNadbar;
    }

    public List<Nadbar> getNadbars() {
        return nadbars;
    }

    public void setNadbars(List<Nadbar> nadbars) {
        this.nadbars = nadbars;
    }
}
