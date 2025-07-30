package com.example.poolapp.exception.clients;

public class DuplicateClientException extends RuntimeException {
    public DuplicateClientException(String field, String value) {
        super(String.format("Клиент с %s (%s) уже существует", field, value));
    }
}
