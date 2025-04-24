package minipimplytixinfosquad.entidades.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanDTO {
    private Long id;
    private String nombre;
    private Integer maxProductos;
    private Integer maxActivos;
    private Integer maxAlmacenamiento;
    private Integer maxCategoriasProductos;
    private Integer maxCategoriasActivos;
    private Integer maxRelaciones;
    private Number precio;
}

