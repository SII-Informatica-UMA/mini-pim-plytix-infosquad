package minipimplytixinfosquad.entidades.controllers;

import minipimplytixinfosquad.entidades.dtos.CuentaDTO;
import minipimplytixinfosquad.entidades.dtos.CuentaNuevaDTO;
import minipimplytixinfosquad.entidades.entities.Cuenta;
import minipimplytixinfosquad.entidades.entities.Plan;

import java.util.Date;

public class CuentaMapper {

    public static CuentaDTO toDTO(Cuenta cuenta) {
        return CuentaDTO.builder()
                .id(cuenta.getId())
                .nombre(cuenta.getNombre())
                .direccion(cuenta.getDireccion())
                .nif(cuenta.getNif())
                .fechaAlta(cuenta.getFechaAlta())
                .planId(cuenta.getPlan() != null ? cuenta.getPlan().getId() : null)
                .propietarioId(cuenta.getPropietarioId())
                .usuariosIds(cuenta.getUsuariosIds())
                .build();
    }

    public static Cuenta toEntity(CuentaNuevaDTO dto, Plan plan) {
        return Cuenta.builder()
                .nombre(dto.getNombre())
                .direccion(dto.getDireccion())
                .nif(dto.getNif())
                .fechaAlta(new Date())
                .plan(plan)
                .propietarioId(dto.getPropietarioId())
                .usuariosIds(dto.getUsuariosIds())
                .build();
    }
}

