package io.brau.backend.dto;

import java.math.BigDecimal;

public record BeneficioTransferResponse(
        BeneficioResponse beneficioOrigem,
        BeneficioResponse beneficioDestino,
        BigDecimal valorTransferido
) {
}
