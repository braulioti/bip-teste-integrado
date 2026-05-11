package io.brau.backend.controller.docs;

import io.brau.backend.dto.BeneficioRequest;
import io.brau.backend.dto.BeneficioResponse;
import io.brau.backend.exception.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Beneficios", description = "Operacoes de CRUD de beneficios")
public interface BeneficioControllerDocs {
    @Operation(summary = "Listar beneficios")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de beneficios retornada com sucesso")
    })
    ResponseEntity<List<BeneficioResponse>> list();

    @Operation(summary = "Buscar beneficio por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Beneficio encontrado com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Beneficio nao encontrado",
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
                                            """)))
    })
    ResponseEntity<BeneficioResponse> findById(Long id);

    @Operation(summary = "Criar beneficio")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Beneficio criado com sucesso"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados invalidos para criacao do beneficio",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "NomeObrigatorio",
                                            value = """
                                                    {
                                                      "status": 400,
                                                      "message": "O nome do beneficio e obrigatorio.",
                                                      "timestamp": "2026-05-11T15:43:21.123Z"
                                                    }
                                                    """),
                                    @ExampleObject(
                                            name = "ValorInvalido",
                                            value = """
                                                    {
                                                      "status": 400,
                                                      "message": "O valor do beneficio deve ser maior ou igual a zero.",
                                                      "timestamp": "2026-05-11T15:43:21.123Z"
                                                    }
                                                    """)
                            }))
    })
    ResponseEntity<BeneficioResponse> create(BeneficioRequest request);

    @Operation(summary = "Atualizar beneficio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Beneficio atualizado com sucesso"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados invalidos para atualizacao do beneficio",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "PayloadInvalido",
                                    value = """
                                            {
                                              "status": 400,
                                              "message": "O status do beneficio e obrigatorio.",
                                              "timestamp": "2026-05-11T15:43:21.123Z"
                                            }
                                            """))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Beneficio nao encontrado",
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
                                            """)))
    })
    ResponseEntity<BeneficioResponse> update(Long id, BeneficioRequest request);

    @Operation(summary = "Remover beneficio")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Beneficio removido com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Beneficio nao encontrado",
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
                                            """)))
    })
    ResponseEntity<Void> delete(Long id);
}
