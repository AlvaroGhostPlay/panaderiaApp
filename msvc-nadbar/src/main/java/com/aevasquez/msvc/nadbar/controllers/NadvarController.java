package com.aevasquez.msvc.nadbar.controllers;

import com.aevasquez.msvc.nadbar.model.Nadbar;
import com.aevasquez.msvc.nadbar.services.NadbarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@EnableMethodSecurity
@RequestMapping("/nadbar")
public class NadvarController {

    @Autowired
    private NadbarService nadbarService;

    @GetMapping("/getNadbarById")
    public ResponseEntity<?> getNadbarById(@RequestParam("id") UUID id){
        return ResponseEntity.ok().body(nadbarService.getNadbarById(id));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/getNadbars")
    public ResponseEntity<?> getNadbar(@RequestParam("typeNadbar") String typeNadbar){
        return ResponseEntity.ok().body(nadbarService.getNadbarByType(typeNadbar));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNadbar(@RequestBody Nadbar nadbarRquest){
        Optional<Nadbar> nadbar= nadbarService.createNadbar(nadbarRquest);
        if (nadbar.isPresent()){
            return ResponseEntity.ok().body(nadbar);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateNadbar(@RequestBody Nadbar nadbarRquest, @RequestParam("id") UUID id){
        Optional<Nadbar> nadbar= nadbarService.updateNadbarById(nadbarRquest, id);
        if (nadbar.isPresent()){
            return ResponseEntity.ok().body(nadbar);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteNadbar(@RequestParam("id") UUID id){
        Optional<Nadbar> nadbar= nadbarService.deleteNadbarById(id);
        if (nadbar.isPresent()){
            return ResponseEntity.ok().body(nadbar);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}