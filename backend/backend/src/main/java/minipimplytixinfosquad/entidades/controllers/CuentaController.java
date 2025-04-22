package minipimplytixinfosquad.entidades.controllers;

import minipimplytixinfosquad.entidades.entities.Cuenta;
import minipimplytixinfosquad.entidades.services.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuenta")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    // GET /cuenta
    @GetMapping
    public ResponseEntity<List<Cuenta>> listarCuentas() {
        return ResponseEntity.ok(cuentaService.listarCuentas());
    }

    // POST /cuenta
    @PostMapping
    public ResponseEntity<Cuenta> crearCuenta(@RequestBody Cuenta cuenta) {
        return ResponseEntity.ok(cuentaService.crearCuenta(cuenta));
    }

    // PUT /cuenta/{idCuenta}
    @PutMapping("/{idCuenta}")
    public ResponseEntity<Cuenta> actualizarCuenta(
            @PathVariable Long idCuenta,
            @RequestBody Cuenta datosActualizados) {
        return ResponseEntity.ok(cuentaService.actualizarCuenta(idCuenta, datosActualizados));
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
