package minipimplytixinfosquad.entidades.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CuentaDTO {
    private Long id;
    private String nombre;
    private String direccion;
    private String nif;
    private Date fechaAlta;
    private PlanDTO plan;
}
