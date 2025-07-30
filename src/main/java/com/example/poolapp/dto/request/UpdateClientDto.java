package com.example.poolapp.dto.request;

import com.example.poolapp.model.Client;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateClientDto {
    @NotNull(message = "id является обязательным")
    private Long id;

    @NotBlank(message = "Имя является обязательным")
    private String name;

    @NotBlank(message = "Email является обязательным")
    @Email(message = "Email должен быть в формате test@...")
    private String email;

    @NotBlank(message = "Телефон является обязательным")
    @Pattern(regexp = "^\\+?[0-9\\-\\s()]{7,20}$",
            message = "Неверный формат телефона, правильный: +79159999999")
    private String phone;

    public void updateEntity(Client client) {
        client.setName(this.name);
        client.setEmail(this.email);
        client.setPhone(this.phone);
    }
}
