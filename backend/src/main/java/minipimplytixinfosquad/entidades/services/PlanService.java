package minipimplytixinfosquad.entidades.services;

import minipimplytixinfosquad.entidades.entities.Plan;
import minipimplytixinfosquad.entidades.repositories.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    // GET /plan
    public List<Plan> listarPlanes() {
        return planRepository.findAll();
    }

    // POST /plan
    public Plan crearPlan(Plan plan) {
        return planRepository.save(plan);
    }

    // PUT /plan/{idPlan}
    public Plan actualizarPlan(Long idPlan, Plan datosActualizados) {
        return planRepository.findById(idPlan)
                .map(plan -> {
                    plan.setNombre(datosActualizados.getNombre());
                    plan.setMaxProductos(datosActualizados.getMaxProductos());
                    plan.setMaxActivos(datosActualizados.getMaxActivos());
                    plan.setMaxAlmacenamiento(datosActualizados.getMaxAlmacenamiento());
                    plan.setMaxCategoriasProductos(datosActualizados.getMaxCategoriasProductos());
                    plan.setMaxCategoriasActivos(datosActualizados.getMaxCategoriasActivos());
                    plan.setMaxRelaciones(datosActualizados.getMaxRelaciones());
                    plan.setPrecio(datosActualizados.getPrecio());
                    return planRepository.save(plan);
                })
                .orElseThrow(() -> new RuntimeException("Plan no encontrado con id: " + idPlan));
    }

    // DELETE /plan/{idPlan}
    public void eliminarPlan(Long idPlan) {
        planRepository.deleteById(idPlan);
    }

    public Optional<Plan> obtenerPlanPorId(Long idPlan) {
        return planRepository.findById(idPlan);
    }
}
