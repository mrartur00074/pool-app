package com.example.poolapp.service;

import com.example.poolapp.dto.ClientDto;
import com.example.poolapp.dto.request.CreateClientDto;
import com.example.poolapp.dto.request.UpdateClientDto;
import com.example.poolapp.dto.response.ClientShortDto;
import com.example.poolapp.exception.clients.ClientNotFoundException;
import com.example.poolapp.exception.clients.DuplicateClientException;
import com.example.poolapp.model.Client;
import com.example.poolapp.repository.ClientRepository;
import com.example.poolapp.service.Impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client testClient;
    private CreateClientDto createDto;
    private UpdateClientDto updateDto;

    @BeforeEach
    void setUp() {
        testClient = new Client(1L, "John Doe", "john@example.com", "+123456789", LocalDateTime.now(), new ArrayList<>());
        createDto = new CreateClientDto("John Doe", "john@example.com", "+123456789");
        updateDto = new UpdateClientDto(1L, "John Updated", "john@example.com", "+123456789");
    }

    @Test
    void getAllClients_ReturnsList() {
        ClientShortDto shortDto = new ClientShortDto(1L, "John Doe");
        when(clientRepository.findAllClientShorts()).thenReturn(Arrays.asList(shortDto));

        List<ClientShortDto> result = clientService.getAllClients();

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void getClientById_ReturnsClient() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));

        ClientDto result = clientService.getClientById(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void getClientById_ThrowsNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.getClientById(1L));
    }

    @Test
    void addClient_CreatesNewClient() {
        when(clientRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(clientRepository.existsByPhone("+123456789")).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenReturn(testClient);

        ClientDto result = clientService.addClient(createDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void addClient_ThrowsDuplicateEmail() {
        when(clientRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(DuplicateClientException.class, () -> clientService.addClient(createDto));
    }

    @Test
    void updateClient_UpdatesExistingClient() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));

        ClientDto result = clientService.updateClient(updateDto);

        assertNotNull(result);
        assertEquals("John Updated", result.getName());
    }

    @Test
    void updateClient_ThrowsNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.updateClient(updateDto));
    }

    @Test
    void getClientOrThrow_ReturnsClient() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));

        Client result = clientService.getClientOrThrow(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }
}
