package br.com.navdata.flowcash.controller;

import br.com.navdata.flowcash.dto.response.UserProfileResponse;
import br.com.navdata.flowcash.service.AuthApiClient;
import br.com.navdata.flowcash.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthApiClient authApiClient;
    private final ObjectMapper objectMapper;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMe(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("X-System-Name") String systemName
    ) throws JsonProcessingException {
        ResponseEntity<String> authenticatedUserResponse =
                authApiClient.getAuthenticatedUser(authorization, systemName);
        if (!authenticatedUserResponse.getStatusCode().is2xxSuccessful()) {
            throw new ResponseStatusException(
                    HttpStatus.valueOf(authenticatedUserResponse.getStatusCode().value()),
                    "Não foi possível obter o usuário autenticado"
            );
        }

        String responseBody = authenticatedUserResponse.getBody();
        if (responseBody == null || responseBody.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "O serviço de autenticação retornou uma resposta vazia"
            );
        }

        AuthenticatedUser authenticatedUser = objectMapper.readValue(responseBody, AuthenticatedUser.class);
        if (authenticatedUser.id() == null || authenticatedUser.email() == null || authenticatedUser.email().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "O serviço de autenticação retornou dados de usuário incompletos"
            );
        }

        UserProfileResponse profile = userService.getOrCreateProfile(
                String.valueOf(authenticatedUser.id()),
                authenticatedUser.email(),
                authenticatedUser.name(),
                null
        );
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/partner")
    public ResponseEntity<UserProfileResponse> invitePartner(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody br.com.navdata.flowcash.dto.request.PartnerRequest request
    ) {
        return ResponseEntity.ok(userService.invitePartner(userId, request.email(), request.name()));
    }

    @DeleteMapping("/partner")
    public ResponseEntity<UserProfileResponse> removePartner(
            @RequestHeader("X-User-Id") String userId
    ) {
        return ResponseEntity.ok(userService.removePartner(userId));
    }

    private record AuthenticatedUser(Integer id, String name, String email) {}
}
