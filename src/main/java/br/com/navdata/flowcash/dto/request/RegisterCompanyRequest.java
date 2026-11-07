package br.com.navdata.flowcash.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterCompanyRequest(
        @NotBlank String companyName,
        @NotBlank String documento,
        @NotBlank String localizacao,
        @NotBlank String adminName,
        @NotBlank @Email String email,
        @NotBlank String password
) {}
