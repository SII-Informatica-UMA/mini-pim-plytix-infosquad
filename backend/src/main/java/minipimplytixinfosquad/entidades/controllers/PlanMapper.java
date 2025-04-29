package minipimplytixinfosquad.entidades.controllers;

import java.math.BigDecimal;

import minipimplytixinfosquad.entidades.dtos.PlanDTO;
import minipimplytixinfosquad.entidades.entities.Plan;

public class PlanMapper {

    public static PlanDTO toDTO(Plan plan) {
        if (plan == null) return null;


        return PlanDTO.builder()
            .id(plan.getId())
            .nombre(plan.getNombre())
            .maxProductos(plan.getMaxProductos())
            .maxActivos(plan.getMaxActivos())
            .maxAlmacenamiento(plan.getMaxAlmacenamiento())
            .maxCategoriasProductos(plan.getMaxCategoriasProductos())
            .maxCategoriasActivos(plan.getMaxCategoriasActivos())
            .maxRelaciones(plan.getMaxRelaciones())
            .precio(plan.getPrecio() != null ? BigDecimal.valueOf(plan.getPrecio().doubleValue()) : BigDecimal.ZERO)
            .build();
    }

    public static Plan toEntity(PlanDTO dto) {
        if (dto == null) return null;

        Plan plan = new Plan();
        plan.setId(dto.getId());
        plan.setNombre(dto.getNombre());
        plan.setMaxProductos(dto.getMaxProductos());
        plan.setMaxActivos(dto.getMaxActivos());
        plan.setMaxAlmacenamiento(dto.getMaxAlmacenamiento());
        plan.setMaxCategoriasProductos(dto.getMaxCategoriasProductos());
        plan.setMaxCategoriasActivos(dto.getMaxCategoriasActivos());
        plan.setMaxRelaciones(dto.getMaxRelaciones());
        plan.setPrecio(dto.getPrecio());

        return plan;
    }
}
