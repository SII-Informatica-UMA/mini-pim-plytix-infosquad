package minipimplytixinfosquad.entidades.services;

import minipimplytixinfosquad.entidades.clients.ProductosClient;
import minipimplytixinfosquad.entidades.entities.Cuenta;
import minipimplytixinfosquad.entidades.entities.Plan;
import minipimplytixinfosquad.entidades.exceptions.CuentaConRecursosException;
import minipimplytixinfosquad.entidades.repositories.CuentaRepository;
import minipimplytixinfosquad.entidades.repositories.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private ProductosClient productosClient;

    // GET /cuenta
    public List<Cuenta> listarCuentas() {
        return cuentaRepository.findAll();
    }

    // POST /cuenta
    public Cuenta crearCuenta(Cuenta cuenta, Long idUsuario) {
        cuenta.setPropietarioId(idUsuario);
        cuenta.setUsuariosIds(List.of(idUsuario));
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
    
        if (cuenta.getUsuariosIds() == null || !cuenta.getUsuariosIds().contains(nuevoPropietarioId)) {
            cuenta.getUsuariosIds().add(nuevoPropietarioId);
        }
    
        cuentaRepository.save(cuenta);
    }

    // GET /cuenta/{idCuenta}/usuarios
    public List<Long> obtenerUsuariosDeCuenta(Long idCuenta) {
        return cuentaRepository.findById(idCuenta)
                .map(Cuenta::getUsuariosIds)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con id: " + idCuenta));
    }

    public void actualizarUsuarios(Long idCuenta, List<Long> nuevosUsuarios) {
        Cuenta cuenta = cuentaRepository.findById(idCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con id: " + idCuenta));
    
        // Siempre incluye al propietario
        Long propietarioId = cuenta.getPropietarioId();
        List<Long> listaFinal = new ArrayList<>(nuevosUsuarios);
    
        if (propietarioId != null && !listaFinal.contains(propietarioId)) {
            listaFinal.add(propietarioId);
        }
    
        cuenta.setUsuariosIds(listaFinal);
        cuentaRepository.save(cuenta);
    }

    public Optional<Cuenta> obtenerCuentaPorId(Long idCuenta) {
        return cuentaRepository.findById(idCuenta);
    }

    
    public void eliminarCuentaSiNoTieneRecursos(Long idCuenta, String jwt) {

        if (productosClient.cuentaTieneProductos(idCuenta, jwt) ||
            productosClient.cuentaTieneCategorias(idCuenta, jwt) ||
            productosClient.cuentaTieneRelaciones(idCuenta, jwt) ||
            productosClient.cuentaTieneActivos(idCuenta, jwt)) {

            throw new CuentaConRecursosException();  
        }

        cuentaRepository.deleteById(idCuenta);
    }
}

