package io.brau.ejb.service;

import io.brau.ejb.entity.Beneficio;
import io.brau.ejb.exception.BeneficioNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Stateless
public class BeneficioEjbService {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Beneficio> list() {
        return entityManager.createQuery("select b from Beneficio b order by b.id", Beneficio.class)
                .getResultList();
    }

    public Beneficio findById(Long id) {
        return findManagedBeneficio(id);
    }

    @Transactional
    public Beneficio create(Beneficio beneficio) {
        validateBeneficio(beneficio);
        entityManager.persist(beneficio);
        entityManager.flush();
        return beneficio;
    }

    @Transactional
    public Beneficio update(Long id, Beneficio beneficio) {
        validateBeneficio(beneficio);

        Beneficio existing = findManagedBeneficio(id);
        existing.setNome(beneficio.getNome());
        existing.setDescricao(beneficio.getDescricao());
        existing.setValor(beneficio.getValor());
        existing.setAtivo(beneficio.getAtivo());

        entityManager.flush();
        return existing;
    }

    @Transactional
    public void delete(Long id) {
        entityManager.remove(findManagedBeneficio(id));
    }

    @Transactional(rollbackOn = Exception.class)
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        validateTransferIdentifiers(fromId, toId, amount);

        Long firstId = fromId < toId ? fromId : toId;
        Long secondId = Objects.equals(firstId, fromId) ? toId : fromId;

        Beneficio first = findManagedBeneficio(firstId, LockModeType.PESSIMISTIC_WRITE);
        Beneficio second = findManagedBeneficio(secondId, LockModeType.PESSIMISTIC_WRITE);

        Beneficio from = Objects.equals(first.getId(), fromId) ? first : second;
        Beneficio to = Objects.equals(first.getId(), toId) ? first : second;

        validateTransfer(from, to, amount);

        from.setValor(from.getValor().subtract(amount));
        to.setValor(to.getValor().add(amount));

        entityManager.flush();
    }

    public void validateTransfer(Beneficio from, Beneficio to, BigDecimal amount) {
        validateTransferRequest(from, to, amount);
        validateActive(from, "origem");
        validateActive(to, "destino");
        validateSufficientBalance(from, amount);
    }

    private Beneficio findManagedBeneficio(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O id do beneficio e obrigatorio.");
        }

        Beneficio beneficio = entityManager.find(Beneficio.class, id);
        if (beneficio == null) {
            throw new BeneficioNotFoundException(id);
        }

        return beneficio;
    }

    private Beneficio findManagedBeneficio(Long id, LockModeType lockModeType) {
        if (id == null) {
            throw new IllegalArgumentException("O id do beneficio e obrigatorio.");
        }

        Beneficio beneficio = entityManager.find(Beneficio.class, id, lockModeType);
        if (beneficio == null) {
            throw new BeneficioNotFoundException(id);
        }

        return beneficio;
    }

    private void validateBeneficio(Beneficio beneficio) {
        if (beneficio == null) {
            throw new IllegalArgumentException("O beneficio e obrigatorio.");
        }

        if (beneficio.getNome() == null || beneficio.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do beneficio e obrigatorio.");
        }

        if (beneficio.getValor() == null || beneficio.getValor().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor do beneficio deve ser maior ou igual a zero.");
        }

        if (beneficio.getAtivo() == null) {
            throw new IllegalArgumentException("O status do beneficio e obrigatorio.");
        }
    }

    private void validateTransferRequest(Beneficio from, Beneficio to, BigDecimal amount) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Os beneficios de origem e destino sao obrigatorios.");
        }

        if (from.getId() == null || to.getId() == null) {
            throw new IllegalArgumentException("Os ids de origem e destino sao obrigatorios.");
        }

        if (Objects.equals(from.getId(), to.getId())) {
            throw new IllegalArgumentException("Origem e destino devem ser diferentes.");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da transferencia deve ser maior que zero.");
        }

        if (from.getValor() == null || to.getValor() == null) {
            throw new IllegalArgumentException("Os saldos de origem e destino sao obrigatorios.");
        }
    }

    private void validateTransferIdentifiers(Long fromId, Long toId, BigDecimal amount) {
        if (fromId == null || toId == null) {
            throw new IllegalArgumentException("Os ids de origem e destino sao obrigatorios.");
        }

        if (Objects.equals(fromId, toId)) {
            throw new IllegalArgumentException("Origem e destino devem ser diferentes.");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da transferencia deve ser maior que zero.");
        }
    }

    private void validateActive(Beneficio beneficio, String tipo) {
        if (!Boolean.TRUE.equals(beneficio.getAtivo())) {
            throw new IllegalStateException("Beneficio de " + tipo + " esta inativo.");
        }
    }

    private void validateSufficientBalance(Beneficio from, BigDecimal amount) {
        if (from.getValor().compareTo(amount) < 0) {
            throw new IllegalStateException("Saldo insuficiente para transferencia.");
        }
    }
}
