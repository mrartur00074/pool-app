package com.example.poolapp.controller;

import com.example.poolapp.dto.ClientDto;
import com.example.poolapp.dto.request.CreateClientDto;
import com.example.poolapp.dto.request.UpdateClientDto;
import com.example.poolapp.dto.response.ClientShortDto;
import com.example.poolapp.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private ClientDto testClient;
    private ClientShortDto testShortClient;

    @BeforeEach
    void setUp() {
        testClient = new ClientDto(1L, "John Doe", "john@example.com", "+123456789");
        testShortClient = new ClientShortDto(1L, "John Doe");
    }

    @Test
    void getAllClients_ReturnsListOfClients() {
        // Подготовка
        when(clientService.getAllClients()).thenReturn(Arrays.asList(testShortClient));

        // Выполнение
        ResponseEntity<List<ClientShortDto>> response = clientController.getAllClients();

        // Проверка
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("John Doe", response.getBody().get(0).getName());
    }

    @Test
    void getClient_ReturnsClient() {
        when(clientService.getClientById(1L)).thenReturn(testClient);

        ResponseEntity<ClientDto> response = clientController.getClient(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getName());
    }

    @Test
    void addClient_CreatesNewClient() {
        CreateClientDto createDto = new CreateClientDto("John Doe", "john@example.com", "+123456789");
        when(clientService.addClient(createDto)).thenReturn(testClient);

        ClientDto response = clientController.addClient(createDto);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void updateClient_UpdatesExistingClient() {
        UpdateClientDto updateDto = new UpdateClientDto(1L, "John Updated", "john@example.com", "+123456789");
        when(clientService.updateClient(updateDto)).thenReturn(testClient);

        ClientDto response = clientController.updateClient(updateDto);

        assertNotNull(response);
        assertEquals("John Doe", response.getName());
    }
}