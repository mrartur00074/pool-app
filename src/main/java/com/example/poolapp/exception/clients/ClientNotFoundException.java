package com.example.poolapp.exception.clients;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(Long id) {
        super("Клиент не найден с данным id: " + id);
    }
}