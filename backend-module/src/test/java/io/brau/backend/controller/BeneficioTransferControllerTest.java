package io.brau.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.brau.backend.dto.BeneficioResponse;
import io.brau.backend.dto.BeneficioTransferRequest;
import io.brau.backend.dto.BeneficioTransferResponse;
import io.brau.backend.integration.ejb.BeneficioEjbGateway;
import io.brau.backend.service.BeneficioService;
import io.brau.ejb.entity.Beneficio;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class BeneficioTransferControllerTest {

    @Mock
    private BeneficioEjbGateway beneficioEjbGateway;

    private BeneficioTransferController controller;

    @BeforeEach
    void setUp() {
        controller = new BeneficioTransferController(new BeneficioService(beneficioEjbGateway));
    }

    @Test
    void shouldReturnOkWhenTransferSucceeds() {
        BeneficioTransferRequest request = new BeneficioTransferRequest(1L, 2L, new BigDecimal("10.00"));
        when(beneficioEjbGateway.findById(1L))
                .thenReturn(beneficio(1L, "Origem", null, new BigDecimal("90.00"), true, 1L));
        when(beneficioEjbGateway.findById(2L))
                .thenReturn(beneficio(2L, "Destino", null, new BigDecimal("60.00"), true, 1L));

        ResponseEntity<BeneficioTransferResponse> response = controller.transfer(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(new BigDecimal("10.00"), response.getBody().valorTransferido());
        verify(beneficioEjbGateway).transfer(1L, 2L, new BigDecimal("10.00"));
        verify(beneficioEjbGateway).findById(1L);
        verify(beneficioEjbGateway).findById(2L);
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
