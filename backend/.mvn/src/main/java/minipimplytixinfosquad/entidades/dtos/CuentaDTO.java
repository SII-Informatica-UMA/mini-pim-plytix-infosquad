package minipimplytixinfosquad.entidades.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class CuentaDTO {
    private Long id;
    private String nombre;
    private String direccion;
    private String nif;
    private Date fechaAlta;
    private Long planId;
    private Long propietarioId;
    private List<Long> usuariosIds;
}
