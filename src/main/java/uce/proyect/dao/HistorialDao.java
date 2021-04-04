package uce.proyect.dao;

import java.util.List;
import javax.ejb.Local;
import uce.proyect.domain.HistorialMedico;

public interface HistorialDao {
    
    public List<HistorialMedico> findAllHistorialMedicos();

    public HistorialMedico findHistorialMedicoById(HistorialMedico historialMedico); //Se toma el Id del Usuario

    public HistorialMedico findHistorialMedicoByCodigo(HistorialMedico historialMedico);

    public void insertHistorialMedico(HistorialMedico historialMedico);

    public void updateHistorialMedico(HistorialMedico historialMedico);

    public void deleteHistorialMedico(HistorialMedico historialMedico);
    
}
