package br.com.navdata.flowcash.controller;

import br.com.navdata.flowcash.dto.response.UserProfileResponse;
import br.com.navdata.flowcash.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMe(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String photoUrl
    ) {
        return ResponseEntity.ok(userService.getOrCreateProfile(userId, email, name, photoUrl));
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
}
