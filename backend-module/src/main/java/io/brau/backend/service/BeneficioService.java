package io.brau.backend.service;

import io.brau.backend.dto.BeneficioRequest;
import io.brau.backend.dto.BeneficioResponse;
import io.brau.backend.dto.BeneficioTransferRequest;
import io.brau.backend.dto.BeneficioTransferResponse;
import io.brau.backend.integration.ejb.BeneficioEjbGateway;
import io.brau.ejb.entity.Beneficio;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class BeneficioService {
    private final BeneficioEjbGateway beneficioEjbGateway;

    public BeneficioService(BeneficioEjbGateway beneficioEjbGateway) {
        this.beneficioEjbGateway = beneficioEjbGateway;
    }

    public List<BeneficioResponse> list() {
        return beneficioEjbGateway.list().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public BeneficioResponse findById(Long id) {
        return toResponse(beneficioEjbGateway.findById(id));
    }

    public BeneficioResponse create(BeneficioRequest request) {
        return toResponse(beneficioEjbGateway.create(toEntity(request)));
    }

    public BeneficioResponse update(Long id, BeneficioRequest request) {
        return toResponse(beneficioEjbGateway.update(id, toEntity(request)));
    }

    public void delete(Long id) {
        beneficioEjbGateway.delete(id);
    }

    public BeneficioTransferResponse transfer(BeneficioTransferRequest request) {
        beneficioEjbGateway.transfer(request.beneficioOrigemId(), request.beneficioDestinoId(), request.valor());

        Beneficio origem = beneficioEjbGateway.findById(request.beneficioOrigemId());
        Beneficio destino = beneficioEjbGateway.findById(request.beneficioDestinoId());

        return new BeneficioTransferResponse(
                toResponse(origem),
                toResponse(destino),
                request.valor());
    }

    private Beneficio toEntity(BeneficioRequest request) {
        Beneficio beneficio = new Beneficio();
        beneficio.setNome(request.nome());
        beneficio.setDescricao(request.descricao());
        beneficio.setValor(request.valor());
        beneficio.setAtivo(request.ativo());
        return beneficio;
    }

    private BeneficioResponse toResponse(Beneficio beneficio) {
        return new BeneficioResponse(
                beneficio.getId(),
                beneficio.getNome(),
                beneficio.getDescricao(),
                beneficio.getValor(),
                beneficio.getAtivo(),
                beneficio.getVersion());
    }
}
