package uce.proyect.dao;

import java.util.List;
import uce.proyect.domain.Cita;

public interface CitaDao {

    public List<Cita> findAllCitas();

    public Cita findCitaById(Cita cita);

    public Cita findCitaByCodigo(Cita cita);

    public List<Cita> findCitaByEstado(Cita cita);

    public void insertCita(Cita cita);

    public void updateCita(Cita cita);

    public void deleteCita(Cita cita);
    
    public Cita findByfechaCitaAndPaciente(Cita cita);

}
