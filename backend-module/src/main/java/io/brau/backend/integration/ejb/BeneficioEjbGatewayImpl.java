package io.brau.backend.integration.ejb;

import io.brau.ejb.entity.Beneficio;
import io.brau.ejb.service.BeneficioEjbService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BeneficioEjbGatewayImpl implements BeneficioEjbGateway {
    private final BeneficioEjbService beneficioEjbService;

    public BeneficioEjbGatewayImpl(BeneficioEjbService beneficioEjbService) {
        this.beneficioEjbService = beneficioEjbService;
    }

    @Override
    public List<Beneficio> list() {
        return beneficioEjbService.list();
    }

    @Override
    public Beneficio findById(Long id) {
        return beneficioEjbService.findById(id);
    }

    @Override
    public Beneficio create(Beneficio beneficio) {
        return beneficioEjbService.create(beneficio);
    }

    @Override
    public Beneficio update(Long id, Beneficio beneficio) {
        return beneficioEjbService.update(id, beneficio);
    }

    @Override
    public void delete(Long id) {
        beneficioEjbService.delete(id);
    }

    @Override
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        beneficioEjbService.transfer(fromId, toId, amount);
    }
}
