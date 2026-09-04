package com.aevasquez.msvc.nadbar.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "nadbars")
public class Nadbar {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_nadbar")
    private UUID idNadbar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_nadbar_id")
    private TypeNadbar typeNadbar;

    private String path;
    private String title;

    public UUID getIdNadbar() {
        return idNadbar;
    }

    public void setIdNadbar(UUID idNadbar) {
        this.idNadbar = idNadbar;
    }

    public TypeNadbar getTypeNadbar() {
        return typeNadbar;
    }

    public void setTypeNadbar(TypeNadbar typeNadbar) {
        this.typeNadbar = typeNadbar;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
