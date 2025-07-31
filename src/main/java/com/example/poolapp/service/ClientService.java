package com.example.poolapp.service;

import java.util.List;

import com.example.poolapp.dto.ClientDto;
import com.example.poolapp.dto.request.CreateClientDto;
import com.example.poolapp.dto.request.UpdateClientDto;
import com.example.poolapp.dto.response.ClientShortDto;
import com.example.poolapp.model.Client;

public interface ClientService {
    List<ClientShortDto> getAllClients();
    ClientDto getClientById(Long id);
    ClientDto addClient(CreateClientDto clientDto);
    ClientDto updateClient(UpdateClientDto clientDto);
    Client getClientOrThrow(Long clientId);
}
