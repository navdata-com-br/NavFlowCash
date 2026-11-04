package br.com.navdata.flowcash.controller;

import br.com.navdata.flowcash.dto.request.PaymentTransactionRequest;
import br.com.navdata.flowcash.service.AuthPaymentClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final AuthPaymentClient authPaymentClient;

    @PostMapping
    public ResponseEntity<String> create(
            @Valid @RequestBody PaymentTransactionRequest payment,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @RequestHeader("X-System-Name") String systemName
    ) {
        return authPaymentClient.createPayment(payment, authorization, systemName);
    }

    @GetMapping("/company/{systemUnitId}")
    public ResponseEntity<String> listByCompany(
            @PathVariable Integer systemUnitId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @RequestHeader("X-System-Name") String systemName
    ) {
        return authPaymentClient.listByCompany(systemUnitId, authorization, systemName);
    }

    @GetMapping("/system/{systemName}")
    public ResponseEntity<String> listBySystem(
            @PathVariable String systemName,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        return authPaymentClient.listBySystem(systemName, authorization);
    }
}
