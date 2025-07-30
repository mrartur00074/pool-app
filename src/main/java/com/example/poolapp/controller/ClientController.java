package com.example.poolapp.controller;

import com.example.poolapp.dto.ClientDto;
import com.example.poolapp.dto.request.CreateClientDto;
import com.example.poolapp.dto.request.UpdateClientDto;
import com.example.poolapp.dto.response.ClientShortDto;
import com.example.poolapp.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0/pool/client")
@RequiredArgsConstructor
@Validated
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/all")
    public ResponseEntity<List<ClientShortDto>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/get")
    public ResponseEntity<ClientDto> getClient(@RequestParam Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDto addClient(@Valid @RequestBody CreateClientDto createDto) {
        return clientService.addClient(createDto);
    }

    @PutMapping("/update")
    public ClientDto updateClient(@Valid @RequestBody UpdateClientDto updateDto) {
        return clientService.updateClient(updateDto);
    }
}
