package minipimplytixinfosquad.entidades.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import minipimplytixinfosquad.entidades.entities.Cuenta;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    
}
