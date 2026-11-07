package br.com.navdata.flowcash.controller;

import br.com.navdata.flowcash.dto.request.AccountRequest;
import br.com.navdata.flowcash.dto.response.AccountResponse;
import br.com.navdata.flowcash.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> list(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(accountService.listByUser(userId));
    }

    @PostMapping
    public ResponseEntity<AccountResponse> create(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody AccountRequest req
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.create(userId, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable String id
    ) {
        accountService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
}
