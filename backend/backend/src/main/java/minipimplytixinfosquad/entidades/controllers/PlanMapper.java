package minipimplytixinfosquad.entidades.controllers;

import minipimplytixinfosquad.entidades.dtos.PlanDTO;
import minipimplytixinfosquad.entidades.entities.Plan;

public class PlanMapper {

    public static PlanDTO toDTO(Plan plan) {
        return PlanDTO.builder()
                .id(plan.getId())
                .nombre(plan.getNombre())
                .maxProductos(plan.getMaxProductos())
                .maxActivos(plan.getMaxActivos())
                .maxAlmacenamiento(plan.getMaxAlmacenamiento())
                .maxCategoriasProductos(plan.getMaxCategoriasProductos())
                .maxCategoriasActivos(plan.getMaxCategoriasActivos())
                .maxRelaciones(plan.getMaxRelaciones())
                .precio(plan.getPrecio())
                .build();
    }
}

