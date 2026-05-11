package io.brau.backend.controller;

import io.brau.backend.dto.BeneficioTransferRequest;
import io.brau.backend.dto.BeneficioTransferResponse;
import io.brau.backend.service.BeneficioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/beneficios/transferencias")
public class BeneficioTransferController {
    private final BeneficioService beneficioService;

    public BeneficioTransferController(BeneficioService beneficioService) {
        this.beneficioService = beneficioService;
    }

    @PostMapping
    public ResponseEntity<BeneficioTransferResponse> transfer(@Valid @RequestBody BeneficioTransferRequest request) {
        return ResponseEntity.ok(beneficioService.transfer(request));
    }
}
