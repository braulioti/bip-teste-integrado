package io.brau.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record BeneficioTransferRequest(
        @NotNull(message = "O id do beneficio de origem e obrigatorio.")
        Long beneficioOrigemId,

        @NotNull(message = "O id do beneficio de destino e obrigatorio.")
        Long beneficioDestinoId,

        @NotNull(message = "O valor da transferencia e obrigatorio.")
        @DecimalMin(value = "0.01", inclusive = true, message = "O valor da transferencia deve ser maior que zero.")
        BigDecimal valor
) {
}
