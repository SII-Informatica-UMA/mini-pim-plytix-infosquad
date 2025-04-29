package minipimplytixinfosquad.entidades.controllers;

import minipimplytixinfosquad.entidades.dtos.CuentaDTO;
import minipimplytixinfosquad.entidades.dtos.CuentaNuevaDTO;
import minipimplytixinfosquad.entidades.dtos.PlanDTO;
import minipimplytixinfosquad.entidades.entities.Cuenta;
import minipimplytixinfosquad.entidades.entities.Plan;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Date;

public class CuentaMapper {

    // Convierte de Entity a DTO (para responses GET)
    public static CuentaDTO toDTO(Cuenta cuenta) {
        return CuentaDTO.builder()
                .id(cuenta.getId())
                .nombre(cuenta.getNombre())
                .direccion(cuenta.getDireccion())
                .nif(cuenta.getNif())
                .fechaAlta(cuenta.getFechaAlta()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate())
                .plan(PlanDTO.builder()
                        .id(cuenta.getPlan().getId())
                        .nombre(cuenta.getPlan().getNombre())
                        .maxProductos(cuenta.getPlan().getMaxProductos())
                        .maxActivos(cuenta.getPlan().getMaxActivos())
                        .maxAlmacenamiento(cuenta.getPlan().getMaxAlmacenamiento())
                        .maxCategoriasProductos(cuenta.getPlan().getMaxCategoriasProductos())
                        .maxCategoriasActivos(cuenta.getPlan().getMaxCategoriasActivos())
                        .maxRelaciones(cuenta.getPlan().getMaxRelaciones())
                        .precio(BigDecimal.valueOf(cuenta.getPlan().getPrecio().doubleValue()))
                        .build())
                .build();
    }

    // Convierte de DTO de creación a Entity (para POST y PUT)
    public static Cuenta toEntity(CuentaNuevaDTO dto, Plan plan) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNombre(dto.getNombre());
        cuenta.setDireccion(dto.getDireccion());
        cuenta.setNif(dto.getNif());

        if (dto.getFechaAlta() != null) {
            cuenta.setFechaAlta(Date.from(dto.getFechaAlta()
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()));
        } else {
            cuenta.setFechaAlta(new Date()); // Si no llega, usa la fecha actual
        }

        cuenta.setPlan(plan);

        // Propietario y usuarios normalmente se tratan aparte
        return cuenta;
    }
}
