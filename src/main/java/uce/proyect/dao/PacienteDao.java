package uce.proyect.dao;

import java.util.List;
import uce.proyect.domain.Paciente;

public interface PacienteDao {

    public List<Paciente> findAllPacientes();

    public Paciente findPacienteById(Paciente paciente);

    public Paciente findPacienteByCi(Paciente paciente);

    public List<Paciente> findPacienteByEstado(Paciente paciente);

    public void insertPaciente(Paciente paciente);

    public void updatePaciente(Paciente paciente);

    public void deletePaciente(Paciente paciente);
}
