package uce.proyect.service;

import java.util.List;
import javax.ejb.Local;
import uce.proyect.domain.HistorialMedico;

/**
 *
 * @author Erick
 */
@Local
public interface HistorialMedicoService {
    
    public List<HistorialMedico> buscarTodosHistorialMedicos();

    public HistorialMedico buscarHistorialMedicoPorId(HistorialMedico historialMedico); //Se toma el Id del Usuario

    public HistorialMedico buscarHistorialMedicoPorCodigo(HistorialMedico historialMedico);

    public void guardarHistorialMedico(HistorialMedico historialMedico);

    public void actualizarHistorialMedico(HistorialMedico historialMedico);

    public void eliminarHistorialMedico(HistorialMedico historialMedico);
    
}
