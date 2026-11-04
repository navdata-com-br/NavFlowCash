package br.com.navdata.flowcash.controller;

import br.com.navdata.flowcash.dto.request.TransactionCreateRequest;
import br.com.navdata.flowcash.dto.request.TransferRequest;
import br.com.navdata.flowcash.dto.response.TransactionResponse;
import br.com.navdata.flowcash.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> list(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return ResponseEntity.ok(transactionService.listByUserAndMonth(userId, year, month));
    }

    @PostMapping
    public ResponseEntity<List<TransactionResponse>> create(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody TransactionCreateRequest req
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.create(userId, req));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody TransferRequest req
    ) {
        transactionService.createTransfer(userId, req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}/toggle-paid")
    public ResponseEntity<TransactionResponse> togglePaid(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable String id
    ) {
        return ResponseEntity.ok(transactionService.togglePaid(userId, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable String id
    ) {
        transactionService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
}
