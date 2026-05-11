package io.brau.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.brau.backend.dto.BeneficioRequest;
import io.brau.backend.dto.BeneficioResponse;
import io.brau.backend.dto.BeneficioTransferRequest;
import io.brau.backend.dto.BeneficioTransferResponse;
import io.brau.backend.integration.ejb.BeneficioEjbGateway;
import io.brau.ejb.entity.Beneficio;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BeneficioServiceTest {

    @Mock
    private BeneficioEjbGateway beneficioEjbGateway;

    @InjectMocks
    private BeneficioService beneficioService;

    @Test
    void shouldMapGatewayListToResponses() {
        Beneficio first = beneficio(1L, "Vale Alimentacao", "Credito mensal", new BigDecimal("750.00"), true, 3L);
        Beneficio second = beneficio(2L, "Vale Cultura", null, new BigDecimal("120.00"), false, 1L);
        when(beneficioEjbGateway.list()).thenReturn(List.of(first, second));

        List<BeneficioResponse> responses = beneficioService.list();

        assertEquals(2, responses.size());
        assertEquals("Vale Alimentacao", responses.get(0).nome());
        assertEquals(new BigDecimal("120.00"), responses.get(1).valor());
        assertNull(responses.get(1).descricao());
        verify(beneficioEjbGateway).list();
    }

    @Test
    void shouldCreateBeneficioFromRequestAndMapResponse() {
        BeneficioRequest request =
                new BeneficioRequest("Auxilio Home Office", "Ajuda de custo", new BigDecimal("200.00"), true);
        Beneficio created = beneficio(10L, request.nome(), request.descricao(), request.valor(), request.ativo(), 0L);
        when(beneficioEjbGateway.create(any(Beneficio.class))).thenReturn(created);

        BeneficioResponse response = beneficioService.create(request);

        ArgumentCaptor<Beneficio> captor = ArgumentCaptor.forClass(Beneficio.class);
        verify(beneficioEjbGateway).create(captor.capture());

        Beneficio sent = captor.getValue();
        assertEquals("Auxilio Home Office", sent.getNome());
        assertEquals("Ajuda de custo", sent.getDescricao());
        assertEquals(new BigDecimal("200.00"), sent.getValor());
        assertEquals(Boolean.TRUE, sent.getAtivo());

        assertEquals(10L, response.id());
        assertEquals("Auxilio Home Office", response.nome());
        assertEquals(0L, response.version());
    }

    @Test
    void shouldUpdateBeneficioUsingGatewayAndReturnMappedResponse() {
        BeneficioRequest request =
                new BeneficioRequest("Plano Odontologico", "Atualizado", new BigDecimal("89.90"), true);
        Beneficio updated = beneficio(7L, request.nome(), request.descricao(), request.valor(), request.ativo(), 4L);
        when(beneficioEjbGateway.update(any(Long.class), any(Beneficio.class))).thenReturn(updated);

        BeneficioResponse response = beneficioService.update(7L, request);

        ArgumentCaptor<Beneficio> captor = ArgumentCaptor.forClass(Beneficio.class);
        verify(beneficioEjbGateway).update(eq(7L), captor.capture());
        assertEquals("Plano Odontologico", captor.getValue().getNome());
        assertEquals(new BigDecimal("89.90"), captor.getValue().getValor());
        assertEquals(7L, response.id());
        assertEquals(4L, response.version());
    }

    @Test
    void shouldDeleteBeneficioUsingGateway() {
        doNothing().when(beneficioEjbGateway).delete(5L);

        beneficioService.delete(5L);

        verify(beneficioEjbGateway).delete(5L);
    }

    @Test
    void shouldTransferValueAndReturnUpdatedBenefits() {
        BeneficioTransferRequest request = new BeneficioTransferRequest(1L, 2L, new BigDecimal("35.50"));
        Beneficio origemAtualizado =
                beneficio(1L, "Origem", "Saldo reduzido", new BigDecimal("114.50"), true, 2L);
        Beneficio destinoAtualizado =
                beneficio(2L, "Destino", "Saldo aumentado", new BigDecimal("235.50"), true, 6L);

        doNothing()
                .when(beneficioEjbGateway)
                .transfer(request.beneficioOrigemId(), request.beneficioDestinoId(), request.valor());
        when(beneficioEjbGateway.findById(1L)).thenReturn(origemAtualizado);
        when(beneficioEjbGateway.findById(2L)).thenReturn(destinoAtualizado);

        BeneficioTransferResponse response = beneficioService.transfer(request);

        verify(beneficioEjbGateway).transfer(1L, 2L, new BigDecimal("35.50"));
        verify(beneficioEjbGateway).findById(1L);
        verify(beneficioEjbGateway).findById(2L);

        assertEquals(new BigDecimal("35.50"), response.valorTransferido());
        assertEquals(origemAtualizado.getId(), response.beneficioOrigem().id());
        assertEquals(destinoAtualizado.getId(), response.beneficioDestino().id());
        assertEquals(new BigDecimal("114.50"), response.beneficioOrigem().valor());
        assertEquals(new BigDecimal("235.50"), response.beneficioDestino().valor());
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
