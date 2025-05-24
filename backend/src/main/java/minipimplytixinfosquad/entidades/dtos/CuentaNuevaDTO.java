package minipimplytixinfosquad.entidades.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuentaNuevaDTO {
    private String nombre;
    private String direccion;
    private String nif;
    private LocalDate fechaAlta;
    private PlanIdDTO plan;    
    private Long propietarioId;
    private List<Long> usuariosIds;

    @Data
    @Builder
    public static class PlanIdDTO {
        private Long id; 
    }

    public Long getPlanId() {
        return plan != null ? plan.getId() : null;
    }
    
}
