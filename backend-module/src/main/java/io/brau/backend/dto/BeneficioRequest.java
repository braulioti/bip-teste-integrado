package io.brau.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record BeneficioRequest(
        @NotBlank(message = "O nome do beneficio e obrigatorio.")
        String nome,

        String descricao,

        @NotNull(message = "O valor do beneficio e obrigatorio.")
        @DecimalMin(value = "0.00", inclusive = true, message = "O valor do beneficio deve ser maior ou igual a zero.")
        BigDecimal valor,

        @NotNull(message = "O status do beneficio e obrigatorio.")
        Boolean ativo
) {
}
