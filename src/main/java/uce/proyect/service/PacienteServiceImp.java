/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uce.proyect.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import uce.proyect.dao.PacienteDao;
import uce.proyect.domain.Paciente;

/**
 *
 * @author Erick
 */
@Stateless
public class PacienteServiceImp implements PacienteService {

    @Inject
    private PacienteDao pacienteDao;
    
    @Override
    public List<Paciente> buscarTodosPacientes() {
        return pacienteDao.findAllPacientes();
    }

    @Override
    public Paciente buscarPacientePorId(Paciente paciente) {
        return pacienteDao.findPacienteById(paciente);
    }

    @Override
    public Paciente buscarPacientePorCi(Paciente paciente) {
        return pacienteDao.findPacienteByCi(paciente);
    }

    @Override
    public List<Paciente> buscarPacientePorEstado(Paciente paciente) {
        return pacienteDao.findPacienteByEstado(paciente);
    }

    @Override
    public void guardarPaciente(Paciente paciente) {
        pacienteDao.insertPaciente(paciente);
    }

    @Override
    public void actuaizarPaciente(Paciente paciente) {
        pacienteDao.updatePaciente(paciente);
    }

    @Override
    public void eliminarPaciente(Paciente paciente) {
        pacienteDao.deletePaciente(paciente);
    }

}
