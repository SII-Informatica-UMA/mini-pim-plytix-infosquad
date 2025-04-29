package minipimplytixinfosquad.entidades.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CuentaDTO {
    private Long id;
    private String nombre;
    private String direccion;
    private String nif;
    private LocalDate fechaAlta;
    private PlanDTO plan;
}
