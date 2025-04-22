package minipimplytixinfosquad.entidades.controllers;

import minipimplytixinfosquad.entidades.entities.Plan;
import minipimplytixinfosquad.entidades.services.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plan")
public class PlanController {

    @Autowired
    private PlanService planService;

    // GET /plan
    @GetMapping
    public ResponseEntity<List<Plan>> listarPlanes() {
        return ResponseEntity.ok(planService.listarPlanes());
    }

    // POST /plan
    @PostMapping
    public ResponseEntity<Plan> crearPlan(@RequestBody Plan plan) {
        return ResponseEntity.ok(planService.crearPlan(plan));
    }

    // PUT /plan/{idPlan}
    @PutMapping("/{idPlan}")
    public ResponseEntity<Plan> actualizarPlan(
            @PathVariable Long idPlan,
            @RequestBody Plan datosActualizados) {
        return ResponseEntity.ok(planService.actualizarPlan(idPlan, datosActualizados));
    }

    // DELETE /plan/{idPlan}
    @DeleteMapping("/{idPlan}")
    public ResponseEntity<Void> eliminarPlan(@PathVariable Long idPlan) {
        planService.eliminarPlan(idPlan);
        return ResponseEntity.noContent().build();
    }
}

