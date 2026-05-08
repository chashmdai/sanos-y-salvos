package cl.smid.match.entity;

public enum MatchStatus {
    /**
     * El motor de IA encontró una coincidencia, pero aún no ha sido revisada por el usuario.
     */
    PENDIENTE,

    /**
     * El dueño de la mascota perdida confirmó que la mascota encontrada es la suya.
     */
    CONFIRMADO_POR_DUEÑO,

    /**
     * El dueño revisó el reporte y determinó que NO es su mascota.
     */
    DESCARTADO
}