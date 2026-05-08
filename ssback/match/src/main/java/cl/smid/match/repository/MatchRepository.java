package cl.smid.match.repository;

import cl.smid.match.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    
    // Busca todos los matches generados para una mascota perdida específica
    List<Match> findByIdReportePerdido(Long idReportePerdido);
    
    // Busca si ya existe un match previo entre un reporte perdido y uno encontrado 
    // (para no generar duplicados si volvems a consultar a la IA)
    boolean existsByIdReportePerdidoAndIdReporteEncontrado(Long idReportePerdido, Long idReporteEncontrado);
}