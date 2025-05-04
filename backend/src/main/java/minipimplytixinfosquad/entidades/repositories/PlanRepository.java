package minipimplytixinfosquad.entidades.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import minipimplytixinfosquad.entidades.entities.Plan;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    
}

