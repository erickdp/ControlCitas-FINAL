package uce.proyect.dao;

import java.util.List;
import uce.proyect.domain.Medico;

public interface MedicoDao {
    
    public List<Medico> findAllMedicos();

    public Medico findMedicoById(Medico medico);
    
    public void updateMedico(Medico medico);
    
}
