package io.brau.backend.config;

import io.brau.ejb.entity.Beneficio;
import io.brau.ejb.service.BeneficioEjbService;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackageClasses = Beneficio.class)
public class EjbModuleConfig {

    @Bean
    public BeneficioEjbService beneficioEjbService() {
        return new BeneficioEjbService();
    }
}
