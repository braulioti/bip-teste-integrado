package io.brau.ejb.exception;

public class BeneficioNotFoundException extends RuntimeException {

    public BeneficioNotFoundException(Long id) {
        super("Beneficio com id " + id + " nao foi encontrado.");
    }
}
