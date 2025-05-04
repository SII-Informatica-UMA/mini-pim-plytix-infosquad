package minipimplytixinfosquad.entidades.controllers;

import jakarta.servlet.http.HttpServletRequest;
import minipimplytixinfosquad.entidades.dtos.CuentaDTO;
import minipimplytixinfosquad.entidades.dtos.CuentaNuevaDTO;
import minipimplytixinfosquad.entidades.dtos.NuevoPropietarioDTO;
import minipimplytixinfosquad.entidades.dtos.UsuarioBasicoDTO;
import minipimplytixinfosquad.entidades.dtos.UsuarioDTO;
import minipimplytixinfosquad.entidades.dtos.UsuarioResumenDTO;
import minipimplytixinfosquad.entidades.entities.Cuenta;
import minipimplytixinfosquad.entidades.entities.Plan;
import minipimplytixinfosquad.entidades.exceptions.CuentaConRecursosException;
import minipimplytixinfosquad.entidades.clients.UsuarioClient;
import minipimplytixinfosquad.entidades.controllers.CuentaMapper;
import minipimplytixinfosquad.entidades.services.CuentaService;
import minipimplytixinfosquad.entidades.services.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cuenta")
public class CuentaController {
    
    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private PlanService planService;

    @Autowired
    private UsuarioClient usuarioClient;

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
        cuenta.setUsuariosIds(List.of(idUsuario)); //lo añade como usuario
        
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
    public ResponseEntity<?> eliminarCuenta(
            @PathVariable Long idCuenta,
            HttpServletRequest request) {

        String token = extraerTokenDeRequest(request);

        try {
            cuentaService.eliminarCuentaSiNoTieneRecursos(idCuenta, token);
            return ResponseEntity.ok().build();   // 200 OK

        } catch (CuentaConRecursosException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
    }


    @GetMapping("/{idCuenta}/propietario")
    public ResponseEntity<?> obtenerPropietario(
            @PathVariable Long idCuenta,
            HttpServletRequest request) {

        Long usuarioActualId = obtenerIdUsuarioActual();
        Cuenta cuenta = cuentaService.obtenerCuentaPorId(idCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        boolean esAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean esAutorizado = esAdmin || cuenta.getUsuariosIds().contains(usuarioActualId);
        if (!esAutorizado) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String token = extraerTokenDeRequest(request);
        UsuarioResumenDTO propietario = usuarioClient
                .obtenerUsuarioPorId(cuenta.getPropietarioId(), token);

        
        UsuarioBasicoDTO dto = new UsuarioBasicoDTO();
        dto.setId(propietario.getId());
        dto.setEmail(propietario.getEmail());
        dto.setNombre(propietario.getNombre());
        dto.setApellido1(propietario.getApellido1());
        dto.setApellido2(propietario.getApellido2());

        return ResponseEntity.ok(dto);     
    }


    private String extraerTokenDeRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new RuntimeException("No se proporcionó token de autorización.");
    }


    @PostMapping("/{idCuenta}/propietario")
    public ResponseEntity<?> actualizarPropietario(
            @PathVariable Long idCuenta,
            @RequestBody NuevoPropietarioDTO dto,
            HttpServletRequest request) {

        String token = extraerTokenDeRequest(request);

        if (dto.getId() == null || dto.getEmail() == null || dto.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body("Debes proporcionar id y email");
        }

        UsuarioResumenDTO nuevoProp;
        try {
            nuevoProp = usuarioClient.obtenerUsuarioPorId(dto.getId(), token);

            // Verificar consistencia email-id
            if (!dto.getEmail().equalsIgnoreCase(nuevoProp.getEmail())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                    .body("El ID y el email no coinciden");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body("Usuario no encontrado");
        }

        
        cuentaService.actualizarPropietario(idCuenta, nuevoProp.getId());

       
        UsuarioBasicoDTO salida = new UsuarioBasicoDTO();
        salida.setId(nuevoProp.getId());
        salida.setEmail(nuevoProp.getEmail());
        salida.setNombre(nuevoProp.getNombre());
        salida.setApellido1(nuevoProp.getApellido1());
        salida.setApellido2(nuevoProp.getApellido2());

        return ResponseEntity.ok(salida);         
    }

    // GET /cuenta/{idCuenta}/usuarios
    @GetMapping("/{idCuenta}/usuarios")
    public ResponseEntity<?> obtenerUsuarios(
            @PathVariable Long idCuenta,
            HttpServletRequest request) {

        Long usuarioActualId = obtenerIdUsuarioActual();
        Cuenta cuenta = cuentaService.obtenerCuentaPorId(idCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        boolean esAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean esPropietario = cuenta.getPropietarioId() != null &&
                                cuenta.getPropietarioId().equals(usuarioActualId);

        if (!esAdmin && !esPropietario) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        
        Set<Long> idsUnicos = new HashSet<>(cuenta.getUsuariosIds());   
        idsUnicos.add(cuenta.getPropietarioId());                       

        
        String token = extraerTokenDeRequest(request);
        
        List<UsuarioResumenDTO> datosCompletos = usuarioClient
        .obtenerUsuariosPorIds(new ArrayList<>(idsUnicos), token);

        List<UsuarioBasicoDTO> usuarios = datosCompletos.stream()
                .map(u -> {
                    UsuarioBasicoDTO dto = new UsuarioBasicoDTO();
                    dto.setId(u.getId());
                    dto.setEmail(u.getEmail());
                    dto.setNombre(u.getNombre());
                    dto.setApellido1(u.getApellido1());
                    dto.setApellido2(u.getApellido2());
                    return dto;
                })
                .toList();
        return ResponseEntity.ok(usuarios);    
    }

    // POST /cuenta/{idCuenta}/usuarios
    @PostMapping("/{idCuenta}/usuarios")
    public ResponseEntity<?> actualizarUsuarios(
            @PathVariable Long idCuenta,
            @RequestBody List<UsuarioDTO> nuevosUsuarios,
            HttpServletRequest request) {

        
        Long usuarioActualId = obtenerIdUsuarioActual();
        Cuenta cuenta = cuentaService.obtenerCuentaPorId(idCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        boolean esAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean esPropietario = cuenta.getPropietarioId() != null &&
                                cuenta.getPropietarioId().equals(usuarioActualId);

        if (!esAdmin && !esPropietario) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        
        String token = extraerTokenDeRequest(request);

        List<UsuarioResumenDTO> usuariosResolvidos = new ArrayList<>();
        for (UsuarioDTO dto : nuevosUsuarios) {

            if (dto.getId() == null && (dto.getEmail() == null || dto.getEmail().isBlank())) {
                return ResponseEntity.badRequest()
                        .body("Cada usuario debe incluir id o email");
            }

            UsuarioResumenDTO usr;
            try {
                if (dto.getId() != null) {
                    usr = usuarioClient.obtenerUsuarioPorId(dto.getId(), token);
                    
                    if (dto.getEmail() != null &&
                        !dto.getEmail().equalsIgnoreCase(usr.getEmail())) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("Id y email no coinciden para el usuario " + dto.getId());
                    }
                } else {
                    usr = usuarioClient.obtenerUsuarioPorEmail(dto.getEmail(), token);
                }
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Usuario no encontrado: " +
                            (dto.getId() != null ? dto.getId() : dto.getEmail()));
            }
            usuariosResolvidos.add(usr);
        }

        
        boolean propietarioIncluido = usuariosResolvidos.stream()
                .anyMatch(u -> u.getId().equals(cuenta.getPropietarioId()));

        if (!propietarioIncluido) {
            
            UsuarioResumenDTO propietario = usuarioClient
                    .obtenerUsuarioPorId(cuenta.getPropietarioId(), token);
            usuariosResolvidos.add(propietario);
        }

        
        List<Long> idsFinales = usuariosResolvidos.stream()
                .map(UsuarioResumenDTO::getId)
                .distinct()
                .toList();

        cuentaService.actualizarUsuarios(idCuenta, idsFinales);

        
        List<UsuarioBasicoDTO> salida = usuariosResolvidos.stream()
                .map(u -> {
                    UsuarioBasicoDTO b = new UsuarioBasicoDTO();
                    b.setId(u.getId());
                    b.setEmail(u.getEmail());
                    b.setNombre(u.getNombre());
                    b.setApellido1(u.getApellido1());
                    b.setApellido2(u.getApellido2());
                    return b;
                })
                .toList();

        return ResponseEntity.ok(salida);
    }

    private Long obtenerIdUsuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); 
        return Long.parseLong(username);
    }

}
