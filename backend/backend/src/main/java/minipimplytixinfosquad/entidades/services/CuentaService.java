package minipimplytixinfosquad.entidades.services;

import minipimplytixinfosquad.entidades.entities.Cuenta;
import minipimplytixinfosquad.entidades.entities.Plan;
import minipimplytixinfosquad.entidades.repositories.CuentaRepository;
import minipimplytixinfosquad.entidades.repositories.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private PlanRepository planRepository;

    // GET /cuenta
    public List<Cuenta> listarCuentas() {
        return cuentaRepository.findAll();
    }

    // POST /cuenta
    public Cuenta crearCuenta(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }

    // PUT /cuenta/{idCuenta}
    public Cuenta actualizarCuenta(Long idCuenta, Cuenta datosActualizados) {
        return cuentaRepository.findById(idCuenta)
                .map(cuenta -> {
                    cuenta.setNombre(datosActualizados.getNombre());
                    cuenta.setDireccion(datosActualizados.getDireccion());
                    cuenta.setNif(datosActualizados.getNif());
                    cuenta.setFechaAlta(datosActualizados.getFechaAlta());
                    cuenta.setPlan(datosActualizados.getPlan());
                    cuenta.setPropietarioId(datosActualizados.getPropietarioId());
                    cuenta.setUsuariosIds(datosActualizados.getUsuariosIds());
                    return cuentaRepository.save(cuenta);
                })
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con id: " + idCuenta));
    }

    // DELETE /cuenta/{idCuenta}
    public void eliminarCuenta(Long idCuenta) {
        cuentaRepository.deleteById(idCuenta);
    }

    // GET /cuenta/{idCuenta}/propietario
    public Long obtenerPropietario(Long idCuenta) {
        return cuentaRepository.findById(idCuenta)
                .map(Cuenta::getPropietarioId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con id: " + idCuenta));
    }

    // POST /cuenta/{idCuenta}/propietario
    public void actualizarPropietario(Long idCuenta, Long nuevoPropietarioId) {
        Cuenta cuenta = cuentaRepository.findById(idCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con id: " + idCuenta));
        cuenta.setPropietarioId(nuevoPropietarioId);
        cuentaRepository.save(cuenta);
    }

    // GET /cuenta/{idCuenta}/usuarios
    public List<Long> obtenerUsuariosDeCuenta(Long idCuenta) {
        return cuentaRepository.findById(idCuenta)
                .map(Cuenta::getUsuariosIds)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con id: " + idCuenta));
    }

    // POST /cuenta/{idCuenta}/usuarios
    public void actualizarUsuarios(Long idCuenta, List<Long> nuevosUsuarios) {
        Cuenta cuenta = cuentaRepository.findById(idCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con id: " + idCuenta));
        cuenta.setUsuariosIds(nuevosUsuarios);
        cuentaRepository.save(cuenta);
    }

    // (opcional) para usar desde los controladores si necesitas cargar una cuenta con seguridad
    public Optional<Cuenta> obtenerCuentaPorId(Long idCuenta) {
        return cuentaRepository.findById(idCuenta);
    }
}

