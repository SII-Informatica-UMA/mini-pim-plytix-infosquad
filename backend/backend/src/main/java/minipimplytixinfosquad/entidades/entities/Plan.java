package minipimplytixinfosquad.entidades.entities;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "cuentas")
@ToString(exclude = "cuentas")
@Entity
public class Plan {

    @Id
    @GeneratedValue
    private Long id;

    private String nombre;
    private Integer maxProductos;
    private Integer maxActivos;
    private Integer maxAlmacenamiento;
    private Integer maxCategoriasProductos;
    private Integer maxCategoriasActivos;
    private Integer maxRelaciones;
    private Number precio;

    // ✅ Relación inversa: un Plan tiene muchas Cuentas
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    private List<Cuenta> cuentas;
}
