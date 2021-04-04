
package uce.proyect.service;

import java.util.List;
import javax.ejb.Local;
import uce.proyect.domain.Paciente;

/**
 *
 * @author Erick
 */
@Local
public interface PacienteService {
    
    public List<Paciente> buscarTodosPacientes();

    public Paciente buscarPacientePorId(Paciente paciente);

    public Paciente buscarPacientePorCi(Paciente paciente);

    public List<Paciente> buscarPacientePorEstado(Paciente paciente);

    public void guardarPaciente(Paciente paciente);

    public void actuaizarPaciente(Paciente paciente);

    public void eliminarPaciente(Paciente paciente);
    
}
