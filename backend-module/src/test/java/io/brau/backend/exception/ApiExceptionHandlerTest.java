package io.brau.backend.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.brau.ejb.exception.BeneficioNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    void shouldReturnNotFoundResponse() {
        ResponseEntity<ApiErrorResponse> response =
                handler.handleNotFound(new BeneficioNotFoundException(15L));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(404, response.getBody().status());
        assertEquals("Beneficio com id 15 nao foi encontrado.", response.getBody().message());
    }

    @Test
    void shouldReturnBadRequestForIllegalArgument() {
        ResponseEntity<ApiErrorResponse> response =
                handler.handleBadRequest(new IllegalArgumentException("Valor invalido."));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Valor invalido.", response.getBody().message());
    }

    @Test
    void shouldReturnBadRequestForConstraintViolation() {
        ResponseEntity<ApiErrorResponse> response =
                handler.handleBadRequest(new ConstraintViolationException("Violacao de regra.", Set.of()));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Violacao de regra.", response.getBody().message());
    }

    @Test
    void shouldReturnConflictResponse() {
        ResponseEntity<ApiErrorResponse> response =
                handler.handleConflict(new IllegalStateException("Transferencia nao permitida."));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Transferencia nao permitida.", response.getBody().message());
    }
}
