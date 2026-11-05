package br.com.navdata.flowcash.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank @Email String email,
    @NotBlank String password,
    String name,
    String photoUrl,
    String systemName
) {}
