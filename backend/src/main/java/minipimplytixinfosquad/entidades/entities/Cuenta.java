package minipimplytixinfosquad.entidades.entities;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

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
   
    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL)
    private Set<Plan> planes;

}

