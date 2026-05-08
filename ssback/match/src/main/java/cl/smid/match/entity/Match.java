package cl.smid.match.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID del reporte de la mascota que se perdió (viene del ms-mascotas)
    @Column(name = "id_reporte_perdido", nullable = false)
    private Long idReportePerdido;

    // ID del reporte de la mascota que alguien encontró (viene del ms-mascotas)
    @Column(name = "id_reporte_encontrado", nullable = false)
    private Long idReporteEncontrado;

    // Qué tan seguros estamos de que es la misma mascota (ej. 85.5)
    @Column(name = "porcentaje_similitud", nullable = false)
    private Double porcentajeSimilitud;

    // La justificación que nos entregará la Inteligencia Artificial.
    // Usamos TEXT porque la IA puede dar respuestas largas y VARCHAR(255) se quedaría corto.
    @Column(name = "razonamiento", columnDefinition = "TEXT", nullable = false)
    private String razonamiento;

    // Estado actual del Match. Se guarda como String en la BD para que sea legible (PENDIENTE, etc.)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private MatchStatus estado = MatchStatus.PENDIENTE;

    // Auditoría automática de creación
    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    // Auditoría automática de última actualización
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}