package minipimplytixinfosquad.entidades.entities;

import java.util.Objects;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
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
   
    @ManyToOne
    @JoinColumn(name = "cuenta_id")  // Aquí estamos indicando la clave foránea
    private Cuenta cuenta;

}