package com.example.poolapp.repository;

import com.example.poolapp.dto.response.ClientShortDto;
import com.example.poolapp.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    @Query("SELECT NEW com.example.poolapp.dto.response.ClientShortDto(c.id, c.name) FROM Client c")
    List<ClientShortDto> findAllClientShorts();
}