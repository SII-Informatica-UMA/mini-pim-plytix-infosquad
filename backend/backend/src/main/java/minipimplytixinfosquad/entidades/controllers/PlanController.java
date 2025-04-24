package minipimplytixinfosquad.entidades.controllers;

import jakarta.servlet.http.HttpServletRequest;
import minipimplytixinfosquad.entidades.dtos.CuentaDTO;
import minipimplytixinfosquad.entidades.dtos.CuentaNuevaDTO;
import minipimplytixinfosquad.entidades.dtos.PlanDTO;
import minipimplytixinfosquad.entidades.entities.Cuenta;
import minipimplytixinfosquad.entidades.entities.Plan;
import minipimplytixinfosquad.entidades.controllers.CuentaMapper;
import minipimplytixinfosquad.entidades.services.CuentaService;
import minipimplytixinfosquad.entidades.services.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/plan")
public class PlanController {

    @Autowired
    private PlanService planService;

    // GET /plan
    @GetMapping
    public ResponseEntity<List<PlanDTO>> listarPlanes() {
        List<PlanDTO> planes = planService.listarPlanes().stream()
                .map(PlanMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(planes);
    }

    // POST /plan
    @PostMapping
    public ResponseEntity<PlanDTO> crearPlan(@RequestBody Plan plan) {
        Plan creado = planService.crearPlan(plan);
        return ResponseEntity.ok(PlanMapper.toDTO(creado));
    }

    // PUT /plan/{idPlan}
    @PutMapping("/{idPlan}")
    public ResponseEntity<PlanDTO> actualizarPlan(
            @PathVariable Long idPlan,
            @RequestBody Plan plan) {
        Plan actualizado = planService.actualizarPlan(idPlan, plan);
        return ResponseEntity.ok(PlanMapper.toDTO(actualizado));
    }

    // DELETE /plan/{idPlan}
    @DeleteMapping("/{idPlan}")
    public ResponseEntity<Void> eliminarPlan(@PathVariable Long idPlan) {
        planService.eliminarPlan(idPlan);
        return ResponseEntity.noContent().build();
    }
}

