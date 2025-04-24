package minipimplytixinfosquad.entidades.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CuentaNuevaDTO {
    private String nombre;
    private String direccion;
    private String nif;
    private Long planId;
    private Long propietarioId;
    private List<Long> usuariosIds;
}
