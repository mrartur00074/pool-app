package com.example.poolapp.dto.response;

import com.example.poolapp.model.Client;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientShortDto {
    private Long id;
    private String name;

    public static ClientShortDto fromEntity(Client client) {
        return new ClientShortDto(client.getId(), client.getName());
    }
}
