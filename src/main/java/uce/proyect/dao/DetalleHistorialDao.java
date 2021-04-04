package uce.proyect.dao;

import java.util.List;
import uce.proyect.domain.DetalleHistorial;

public interface DetalleHistorialDao {
    
    public List<DetalleHistorial> findAllDetalleHistorials();

    public DetalleHistorial findDetalleHistorialById(DetalleHistorial historialMedico);

    public DetalleHistorial findDetalleHistorialByCodigo(DetalleHistorial historialMedico);

    public void insertDetalleHistorial(DetalleHistorial historialMedico);

    public void updateDetalleHistorial(DetalleHistorial historialMedico);

    public void deleteDetalleHistorial(DetalleHistorial historialMedico);
    
}
