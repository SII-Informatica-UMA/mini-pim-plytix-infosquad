package minipimplytixinfosquad.entidades.controllers;

import jakarta.servlet.http.HttpServletRequest;
import minipimplytixinfosquad.entidades.dtos.CuentaDTO;
import minipimplytixinfosquad.entidades.dtos.CuentaNuevaDTO;
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
@RequestMapping("/cuenta")
public class CuentaController {
    
    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private PlanService planService;

    // GET /cuenta
    @GetMapping
    public ResponseEntity<List<CuentaDTO>> listarCuentas() {
        List<CuentaDTO> cuentas = cuentaService.listarCuentas().stream()
                .map(CuentaMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cuentas);
    }

    // POST /cuenta
    @PostMapping
    public ResponseEntity<CuentaDTO> crearCuenta(@RequestBody CuentaNuevaDTO nuevaCuenta, HttpServletRequest request) {
        System.out.println("Nueva Cuenta Recibida: " + nuevaCuenta);

        if (nuevaCuenta.getPlan() == null || nuevaCuenta.getPlan().getId() == null) {
            throw new RuntimeException("Debes proporcionar el ID del plan en el objeto 'plan'.");
        }

        Long idUsuario = (Long) request.getAttribute("idUsuario");
        if (idUsuario == null) {
            throw new RuntimeException("ID de usuario no disponible en el token.");
        }

        Plan plan = planService.obtenerPlanPorId(nuevaCuenta.getPlan().getId())
                .orElseThrow(() -> new RuntimeException("Plan no encontrado con id: " + nuevaCuenta.getPlan().getId()));

        Cuenta cuenta = CuentaMapper.toEntity(nuevaCuenta, plan);
        cuenta.setPropietarioId(idUsuario);
        cuenta.setUsuariosIds(List.of(idUsuario)); // también lo añade como usuario
        
        Cuenta cuentaGuardada = cuentaService.crearCuenta(cuenta, idUsuario);

        return ResponseEntity.ok(CuentaMapper.toDTO(cuentaGuardada));
    }  


    // PUT /cuenta/{idCuenta}
    @PutMapping("/{idCuenta}")
    public ResponseEntity<CuentaDTO> actualizarCuenta(
            @PathVariable Long idCuenta,
            @RequestBody CuentaNuevaDTO datosActualizados) {

                Long planId = (datosActualizados.getPlan() != null) ? datosActualizados.getPlan().getId() : null;

                if (planId == null) {
                    throw new RuntimeException("El id del plan no puede ser null");
                }
                Plan plan = planService.obtenerPlanPorId(planId)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));


        Cuenta cuentaActualizada = CuentaMapper.toEntity(datosActualizados, plan);
        cuentaActualizada.setId(idCuenta); // aseguramos el ID

        Cuenta actualizada = cuentaService.actualizarCuenta(idCuenta, cuentaActualizada);
        return ResponseEntity.ok(CuentaMapper.toDTO(actualizada));
    }

    // DELETE /cuenta/{idCuenta}
    @DeleteMapping("/{idCuenta}")
    public ResponseEntity<Void> eliminarCuenta(@PathVariable Long idCuenta) {
        cuentaService.eliminarCuenta(idCuenta);
        return ResponseEntity.noContent().build();
    }

    // GET /cuenta/{idCuenta}/propietario
    @GetMapping("/{idCuenta}/propietario")
    public ResponseEntity<Long> obtenerPropietario(@PathVariable Long idCuenta) {
        return ResponseEntity.ok(cuentaService.obtenerPropietario(idCuenta));
    }

    // POST /cuenta/{idCuenta}/propietario
    @PostMapping("/{idCuenta}/propietario")
    public ResponseEntity<Void> actualizarPropietario(
            @PathVariable Long idCuenta,
            @RequestBody Long nuevoPropietarioId) {
        cuentaService.actualizarPropietario(idCuenta, nuevoPropietarioId);
        return ResponseEntity.noContent().build();
    }

    // GET /cuenta/{idCuenta}/usuarios
    @GetMapping("/{idCuenta}/usuarios")
    public ResponseEntity<List<Long>> obtenerUsuarios(@PathVariable Long idCuenta) {
        return ResponseEntity.ok(cuentaService.obtenerUsuariosDeCuenta(idCuenta));
    }

    // POST /cuenta/{idCuenta}/usuarios
    @PostMapping("/{idCuenta}/usuarios")
    public ResponseEntity<Void> actualizarUsuarios(
            @PathVariable Long idCuenta,
            @RequestBody List<Long> nuevosUsuarios) {
        cuentaService.actualizarUsuarios(idCuenta, nuevosUsuarios);
        return ResponseEntity.noContent().build();
    }
}
