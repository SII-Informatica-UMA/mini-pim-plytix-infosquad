package minipimplytixinfosquad.entidades.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanDTO {
    private Long id;
    private String nombre;
    private int maxProductos;
    private int maxActivos;
    private int maxAlmacenamiento;
    private int maxCategoriasProductos;
    private int maxCategoriasActivos;
    private int maxRelaciones;
    private BigDecimal precio;
}
