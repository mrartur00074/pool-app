package com.example.poolapp.controller;

import com.example.poolapp.dto.ClientDto;
import com.example.poolapp.dto.request.CreateClientDto;
import com.example.poolapp.dto.request.UpdateClientDto;
import com.example.poolapp.dto.response.ClientShortDto;
import com.example.poolapp.exception.clients.ClientNotFoundException;
import com.example.poolapp.exception.clients.DuplicateClientException;
import com.example.poolapp.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ClientService clientService(@Mock ClientService clientService) {
            return clientService;
        }
    }

    @Test
    void getAllClients_ReturnsListOfClients() throws Exception {
        ClientShortDto clientShortDto = new ClientShortDto(1L, "John Doe");
        List<ClientShortDto> clients = List.of(clientShortDto);

        when(clientService.getAllClients()).thenReturn(clients);

        mockMvc.perform(get("/api/v0/pool/client/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"));
    }

    @Test
    void getAllClients_ReturnsEmptyList() throws Exception {
        when(clientService.getAllClients()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v0/pool/client/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getClient_ReturnsClient() throws Exception {
        ClientDto clientDto = new ClientDto(1L, "John Doe", "john@example.com", "+123456789");

        when(clientService.getClientById(1L)).thenReturn(clientDto);

        mockMvc.perform(get("/api/v0/pool/client/get").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void getClient_ThrowsNotFoundException() throws Exception {
        when(clientService.getClientById(anyLong())).thenThrow(new ClientNotFoundException(1L));

        mockMvc.perform(get("/api/v0/pool/client/get").param("id", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addClient_ReturnsCreatedClient() throws Exception {
        CreateClientDto createDto = new CreateClientDto("John Doe", "john@example.com", "+123456789");
        ClientDto expectedDto = new ClientDto(1L, "John Doe", "john@example.com", "+123456789");

        when(clientService.addClient(any(CreateClientDto.class))).thenReturn(expectedDto);

        mockMvc.perform(post("/api/v0/pool/client/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void addClient_ThrowsDuplicateEmailException() throws Exception {
        CreateClientDto createDto = new CreateClientDto("John Doe", "john@example.com", "+123456789");

        when(clientService.addClient(any(CreateClientDto.class)))
                .thenThrow(new DuplicateClientException("email", "john@example.com"));

        mockMvc.perform(post("/api/v0/pool/client/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addClient_ThrowsDuplicatePhoneException() throws Exception {
        CreateClientDto createDto = new CreateClientDto("John Doe", "john@example.com", "+123456789");

        when(clientService.addClient(any(CreateClientDto.class)))
                .thenThrow(new DuplicateClientException("phone", "+123456789"));

        mockMvc.perform(post("/api/v0/pool/client/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateClient_ReturnsUpdatedClient() throws Exception {
        UpdateClientDto updateDto = new UpdateClientDto(1L, "John Updated", "john@example.com", "+123456789");
        ClientDto expectedDto = new ClientDto(1L, "John Updated", "john@example.com", "+123456789");

        when(clientService.updateClient(any(UpdateClientDto.class))).thenReturn(expectedDto);

        mockMvc.perform(put("/api/v0/pool/client/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Updated"));
    }

    @Test
    void updateClient_ThrowsNotFoundException() throws Exception {
        UpdateClientDto updateDto = new UpdateClientDto(1L, "John Updated", "john@example.com", "+123456789");

        when(clientService.updateClient(any(UpdateClientDto.class)))
                .thenThrow(new ClientNotFoundException(1L));

        mockMvc.perform(put("/api/v0/pool/client/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateClient_ThrowsDuplicateEmailException() throws Exception {
        UpdateClientDto updateDto = new UpdateClientDto(1L, "John Updated", "john@example.com", "+123456789");

        when(clientService.updateClient(any(UpdateClientDto.class)))
                .thenThrow(new DuplicateClientException("email", "john@example.com"));

        mockMvc.perform(put("/api/v0/pool/client/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isBadRequest());
    }
}
