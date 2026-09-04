package com.aevasquez.msvc.clients.controllers;

import com.aevasquez.msvc.clients.dto.ClientRequest;
import com.aevasquez.msvc.clients.dto.ClientResponseDto;
import com.aevasquez.msvc.clients.model.User;
import com.aevasquez.msvc.clients.services.ClientService;
import com.aevasquez.msvc.clients.services.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping()
    public ResponseEntity<?> getAllClient(){
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable UUID id){
        return ResponseEntity.ok().body(clientService.getClientById(id));
    }

    @GetMapping("/page/{page}")
    public ResponseEntity<?> getClientById(@PathVariable int page, @RequestParam int size){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                clientService.getAllClients(pageable)
        );
    }

    @PostMapping
    public ResponseEntity<?> createClient(@Valid @RequestBody ClientRequest clientRequest){
        ClientResponseDto clientResponseDto = clientService.createClient(clientRequest);
        User user = userServiceImpl.createUser(clientRequest, clientResponseDto);
        return ResponseEntity.ok().body(clientResponseDto);
    }

    @PutMapping
    public ResponseEntity<?> updateClient(@Valid @RequestBody ClientRequest clientRequest, @RequestParam UUID id){
        return ResponseEntity.ok().body(clientService.updateClientById(clientRequest, id));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteClient(@RequestParam UUID id){
        clientService.deleteClientById(id);
        return ResponseEntity.noContent().build();
    }
}
