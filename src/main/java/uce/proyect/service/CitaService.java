package uce.proyect.service;

import java.util.List;
import javax.ejb.Local;
import uce.proyect.domain.Cita;

/**
 *
 * @author Erick
 */
@Local
public interface CitaService {

    public List<Cita> buscarTodasCitas();

    public Cita buscarCitaPorId(Cita cita);

    public Cita buscarCitaPorCodigo(Cita cita);

    public List<Cita> buscarCitaPorEstado(Cita cita);

    public void guardarCita(Cita cita);

    public void actualizarCita(Cita cita);

    public void eliminarCita(Cita cita);
    
    public Cita buscarCitasConcurrentes(Cita cita);
}
