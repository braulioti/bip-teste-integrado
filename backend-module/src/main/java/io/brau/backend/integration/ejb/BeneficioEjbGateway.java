package io.brau.backend.integration.ejb;

import io.brau.ejb.entity.Beneficio;
import java.math.BigDecimal;
import java.util.List;

public interface BeneficioEjbGateway {
    List<Beneficio> list();

    Beneficio findById(Long id);

    Beneficio create(Beneficio beneficio);

    Beneficio update(Long id, Beneficio beneficio);

    void delete(Long id);

    void transfer(Long fromId, Long toId, BigDecimal amount);
}
