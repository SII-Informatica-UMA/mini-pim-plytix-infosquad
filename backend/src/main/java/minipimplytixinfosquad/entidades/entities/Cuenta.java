package minipimplytixinfosquad.entidades.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"plan", "usuariosIds"})
@ToString(exclude = {"plan", "usuariosIds"})
@Entity
public class Cuenta {

    @Id
    @GeneratedValue
    private Long id;

    private String nombre;
    private String direccion;
    private String nif;

    @Column(name = "FECHA_ALTA")
    @Temporal(TemporalType.DATE)
    private Date fechaAlta;

    //  Nueva relación correcta con Plan
    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    //  Nuevo campo: ID del propietario
    private Long propietarioId;

    //  Nuevo campo: lista de IDs de usuarios
    @ElementCollection
    @CollectionTable(name = "cuenta_usuarios", joinColumns = @JoinColumn(name = "cuenta_id"))
    @Column(name = "usuario_id")
    private List<Long> usuariosIds = new ArrayList<>();

}


