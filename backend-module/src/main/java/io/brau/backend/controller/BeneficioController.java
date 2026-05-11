package io.brau.backend.controller;

import io.brau.backend.dto.BeneficioRequest;
import io.brau.backend.dto.BeneficioResponse;
import io.brau.backend.service.BeneficioService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/beneficios")
public class BeneficioController {
    private final BeneficioService beneficioService;

    public BeneficioController(BeneficioService beneficioService) {
        this.beneficioService = beneficioService;
    }

    @GetMapping
    public ResponseEntity<List<BeneficioResponse>> list() {
        return ResponseEntity.ok(beneficioService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BeneficioResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(beneficioService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BeneficioResponse> create(@Valid @RequestBody BeneficioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(beneficioService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BeneficioResponse> update(@PathVariable Long id, @Valid @RequestBody BeneficioRequest request) {
        return ResponseEntity.ok(beneficioService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        beneficioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
