package minipimplytixinfosquad.entidades.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class CuentaNuevaDTO {
    private String nombre;
    private String direccion;
    private String nif;
    private LocalDate fechaAlta;
    private PlanIdDTO plan;    //Aquí ahora es un objeto "plan" con un "id" dentro
    private Long propietarioId;
    private List<Long> usuariosIds;

    @Data
    @Builder
    public static class PlanIdDTO {
        private Long id; // Solo contiene el ID del plan
    }

    public Long getPlanId() {
        return plan != null ? plan.getId() : null;
    }
    
}
