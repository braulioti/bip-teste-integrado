package io.brau.ejb.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import io.brau.ejb.entity.Beneficio;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class BeneficioEjbServiceTest {

    private final BeneficioEjbService service = new BeneficioEjbService();

    @Test
    void shouldValidateTransferWhenRequestIsValid() {
        Beneficio from = beneficio(1L, "100.00", true);
        Beneficio to = beneficio(2L, "50.00", true);

        service.validateTransfer(from, to, new BigDecimal("10.00"));
    }

    @Test
    void shouldRejectTransferWhenBalanceIsInsufficient() {
        Beneficio from = beneficio(1L, "5.00", true);
        Beneficio to = beneficio(2L, "50.00", true);

        assertThrows(IllegalStateException.class,
                () -> service.validateTransfer(from, to, new BigDecimal("10.00")));
    }

    @Test
    void shouldRejectTransferWhenIdsAreEqual() {
        Beneficio from = beneficio(1L, "100.00", true);
        Beneficio to = beneficio(1L, "50.00", true);

        assertThrows(IllegalArgumentException.class,
                () -> service.validateTransfer(from, to, new BigDecimal("10.00")));
    }

    @Test
    void shouldRejectTransferWhenDestinationIsInactive() {
        Beneficio from = beneficio(1L, "100.00", true);
        Beneficio to = beneficio(2L, "50.00", false);

        assertThrows(IllegalStateException.class,
                () -> service.validateTransfer(from, to, new BigDecimal("10.00")));
    }

    @Test
    void shouldRejectTransferWhenOriginBenefitIsMissing() {
        Beneficio to = beneficio(2L, "50.00", true);

        assertThrows(IllegalArgumentException.class,
                () -> service.validateTransfer(null, to, new BigDecimal("10.00")));
    }

    @Test
    void shouldRejectTransferWhenOriginIdIsMissing() {
        Beneficio from = beneficio(null, "100.00", true);
        Beneficio to = beneficio(2L, "50.00", true);

        assertThrows(IllegalArgumentException.class,
                () -> service.validateTransfer(from, to, new BigDecimal("10.00")));
    }

    @Test
    void shouldRejectTransferWhenAmountIsInvalid() {
        Beneficio from = beneficio(1L, "100.00", true);
        Beneficio to = beneficio(2L, "50.00", true);

        assertThrows(IllegalArgumentException.class,
                () -> service.validateTransfer(from, to, BigDecimal.ZERO));
    }

    @Test
    void shouldRejectTransferWhenOriginBalanceIsMissing() {
        Beneficio from = beneficio(1L, null, true);
        Beneficio to = beneficio(2L, "50.00", true);

        assertThrows(IllegalArgumentException.class,
                () -> service.validateTransfer(from, to, new BigDecimal("10.00")));
    }

    private Beneficio beneficio(Long id, String valor, boolean ativo) {
        Beneficio beneficio = new Beneficio();
        beneficio.setId(id);
        beneficio.setValor(valor == null ? null : new BigDecimal(valor));
        beneficio.setAtivo(ativo);
        return beneficio;
    }
}
