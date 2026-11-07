package br.com.navdata.flowcash.controller;

import br.com.navdata.flowcash.dto.request.LoginRequest;
import br.com.navdata.flowcash.dto.request.RegisterCompanyRequest;
import br.com.navdata.flowcash.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody LoginRequest request,
            @RequestHeader(value = "X-System-Name", required = false) String systemNameHeader
    ) {
        String systemName = request.systemName() != null && !request.systemName().isBlank()
                ? request.systemName().trim()
                : systemNameHeader;
        if (systemName == null || systemName.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sistema não informado");
        }
        return authService.login(request, systemName);
    }

    @PostMapping("/register-company")
    public ResponseEntity<String> registerCompany(
            @Valid @RequestBody RegisterCompanyRequest request,
            @RequestHeader(value = "X-System-Name", required = false) String systemNameHeader
    ) {
        if (systemNameHeader == null || systemNameHeader.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sistema não informado");
        }
        return authService.registerCompany(request, systemNameHeader.trim());
    }

    @GetMapping("/me")
    public ResponseEntity<String> getAuthenticatedUser(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader(value = "X-System-Name", required = false) String systemNameHeader
    ) {
        if (systemNameHeader == null || systemNameHeader.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sistema não informado");
        }
        return authService.getAuthenticatedUser(authorization, systemNameHeader.trim());
    }
}
