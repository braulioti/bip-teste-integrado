package io.brau.backend.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiErrorResponse", description = "Formato padrao de erro retornado pela API")
public record ApiErrorResponse(
        @Schema(description = "Codigo HTTP da resposta", example = "400")
        int status,

        @Schema(description = "Mensagem de erro detalhando o problema", example = "O nome do beneficio e obrigatorio.")
        String message,

        @Schema(description = "Data e hora do erro em formato ISO-8601", example = "2026-05-11T15:43:21.123Z")
        String timestamp
) {
}
