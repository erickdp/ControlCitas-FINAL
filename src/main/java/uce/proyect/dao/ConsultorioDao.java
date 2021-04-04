package uce.proyect.dao;

import java.util.List;
import uce.proyect.domain.Consultorio;
import uce.proyect.domain.Empleado;

public interface ConsultorioDao {

    public List<Consultorio> findAllConsultorios();

    public Consultorio findConsultorioById(Consultorio consultorio);
    
    public Consultorio findConsultorioByCodigo(Consultorio consultorio);
    
    public Consultorio findConsultorioByEspecialidad(Consultorio consultorio);

    public List<Consultorio> findConsultorioByEstado(Consultorio consultorio);

    public void insertConsultorio(Consultorio consultorio);

    public void updateConsultorio(Consultorio consultorio);

    public void deleteConsultorio(Consultorio consultorio);
    
    public List<Empleado> generatedQuery(String param);
}
