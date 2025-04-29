package minipimplytixinfosquad.entidades.dtos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
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
