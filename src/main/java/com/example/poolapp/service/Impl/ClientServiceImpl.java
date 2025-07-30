package com.example.poolapp.service.Impl;

import com.example.poolapp.dto.ClientDto;
import com.example.poolapp.dto.request.CreateClientDto;
import com.example.poolapp.dto.request.UpdateClientDto;
import com.example.poolapp.dto.response.ClientShortDto;
import com.example.poolapp.exception.clients.ClientNotFoundException;
import com.example.poolapp.exception.clients.DuplicateClientException;
import com.example.poolapp.model.Client;
import com.example.poolapp.repository.ClientRepository;
import com.example.poolapp.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    final ClientRepository clientRepository;

    @Override
    public List<ClientShortDto> getAllClients() {
        return clientRepository.findAllClientShorts();
    }

    @Override
    public ClientDto getClientById(Long id) {
        return clientRepository.findById(id)
                .map(ClientDto::toDto)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }

    @Override
    @Transactional
    public ClientDto addClient(CreateClientDto clientDto) {
        if (clientRepository.existsByEmail(clientDto.getEmail())) {
            throw new DuplicateClientException("email", clientDto.getEmail());
        }

        if (clientRepository.existsByPhone(clientDto.getPhone())) {
            throw new DuplicateClientException("phone", clientDto.getPhone());
        }

        Client client = clientDto.toEntity();
        Client savedClient = clientRepository.save(client);
        return ClientDto.toDto(savedClient);
    }

    @Override
    @Transactional
    public ClientDto updateClient(UpdateClientDto clientDto) {
        Client client = clientRepository.findById(clientDto.getId())
                .orElseThrow(() -> new ClientNotFoundException(clientDto.getId()));

        if (!client.getEmail().equals(clientDto.getEmail()) &&
                clientRepository.existsByEmail(clientDto.getEmail())) {
            throw new DuplicateClientException("Email", clientDto.getEmail());
        }

        if (!client.getPhone().equals(clientDto.getPhone()) &&
                clientRepository.existsByPhone(clientDto.getPhone())) {
            throw new DuplicateClientException("Телефон", clientDto.getPhone());
        }

        clientDto.updateEntity(client);

        return ClientDto.toDto(client);
    }
}
