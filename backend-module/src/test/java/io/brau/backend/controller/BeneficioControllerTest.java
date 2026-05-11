package io.brau.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.brau.backend.dto.BeneficioRequest;
import io.brau.backend.dto.BeneficioResponse;
import io.brau.backend.integration.ejb.BeneficioEjbGateway;
import io.brau.backend.service.BeneficioService;
import io.brau.ejb.entity.Beneficio;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class BeneficioControllerTest {

    @Mock
    private BeneficioEjbGateway beneficioEjbGateway;

    private BeneficioController controller;

    @BeforeEach
    void setUp() {
        controller = new BeneficioController(new BeneficioService(beneficioEjbGateway));
    }

    @Test
    void shouldReturnOkWhenListingBenefits() {
        List<Beneficio> beneficios = List.of(beneficio(1L, "Vale", "Mensal", new BigDecimal("50.00"), true, 0L));
        when(beneficioEjbGateway.list()).thenReturn(beneficios);

        ResponseEntity<List<BeneficioResponse>> response = controller.list();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(beneficioEjbGateway).list();
    }

    @Test
    void shouldReturnCreatedWhenCreatingBenefit() {
        BeneficioRequest request =
                new BeneficioRequest("Plano", "Beneficio corporativo", new BigDecimal("99.90"), true);
        when(beneficioEjbGateway.create(org.mockito.ArgumentMatchers.any(Beneficio.class)))
                .thenReturn(beneficio(2L, "Plano", "Beneficio corporativo", new BigDecimal("99.90"), true, 0L));

        ResponseEntity<BeneficioResponse> response = controller.create(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2L, response.getBody().id());
        verify(beneficioEjbGateway).create(org.mockito.ArgumentMatchers.any(Beneficio.class));
    }

    @Test
    void shouldReturnNoContentWhenDeletingBenefit() {
        ResponseEntity<Void> response = controller.delete(9L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(beneficioEjbGateway).delete(9L);
    }

    private Beneficio beneficio(
            Long id,
            String nome,
            String descricao,
            BigDecimal valor,
            Boolean ativo,
            Long version) {
        Beneficio beneficio = new Beneficio();
        beneficio.setId(id);
        beneficio.setNome(nome);
        beneficio.setDescricao(descricao);
        beneficio.setValor(valor);
        beneficio.setAtivo(ativo);
        beneficio.setVersion(version);
        return beneficio;
    }
}
