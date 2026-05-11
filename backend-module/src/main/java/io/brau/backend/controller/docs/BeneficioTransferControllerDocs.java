package io.brau.backend.controller.docs;

import io.brau.backend.dto.BeneficioTransferRequest;
import io.brau.backend.dto.BeneficioTransferResponse;
import io.brau.backend.exception.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Transferencias", description = "Operacoes de transferencia entre beneficios")
public interface BeneficioTransferControllerDocs {
    @Operation(summary = "Transferir valor entre beneficios")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transferencia realizada com sucesso"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisicao invalida para transferencia",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "IdsIguais",
                                            value = """
                                                    {
                                                      "status": 400,
                                                      "message": "Origem e destino devem ser diferentes.",
                                                      "timestamp": "2026-05-11T15:43:21.123Z"
                                                    }
                                                    """),
                                    @ExampleObject(
                                            name = "ValorInvalido",
                                            value = """
                                                    {
                                                      "status": 400,
                                                      "message": "O valor da transferencia deve ser maior que zero.",
                                                      "timestamp": "2026-05-11T15:43:21.123Z"
                                                    }
                                                    """)
                            })),
            @ApiResponse(
                    responseCode = "404",
                    description = "Beneficio de origem ou destino nao encontrado",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "BeneficioNaoEncontrado",
                                    value = """
                                            {
                                              "status": 404,
                                              "message": "Beneficio com id 99 nao foi encontrado.",
                                              "timestamp": "2026-05-11T15:43:21.123Z"
                                            }
                                            """))),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflito de regra de negocio na transferencia",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "SaldoInsuficiente",
                                            value = """
                                                    {
                                                      "status": 409,
                                                      "message": "Saldo insuficiente para transferencia.",
                                                      "timestamp": "2026-05-11T15:43:21.123Z"
                                                    }
                                                    """),
                                    @ExampleObject(
                                            name = "BeneficioInativo",
                                            value = """
                                                    {
                                                      "status": 409,
                                                      "message": "Beneficio de destino esta inativo.",
                                                      "timestamp": "2026-05-11T15:43:21.123Z"
                                                    }
                                                    """)
                            }))
    })
    ResponseEntity<BeneficioTransferResponse> transfer(BeneficioTransferRequest request);
}
