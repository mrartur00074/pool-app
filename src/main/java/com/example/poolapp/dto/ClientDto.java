package com.example.poolapp.dto;

import com.example.poolapp.model.Client;
import lombok.Data;

@Data
public class ClientDto {
    private Long id;
    private String name;
    private String email;
    private String phone;

    public static ClientDto toDto(Client client) {
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setName(client.getName());
        dto.setEmail(client.getEmail());
        dto.setPhone(client.getPhone());
        return dto;
    }

    public Client toEntity(ClientDto dto) {
        Client client = new Client();
        client.setId(dto.getId());
        client.setName(dto.getName());
        client.setEmail(dto.getEmail());
        client.setPhone(dto.getPhone());
        return client;
    }
}
