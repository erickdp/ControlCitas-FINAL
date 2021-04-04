/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uce.proyect.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import uce.proyect.dao.ConsultorioDao;
import uce.proyect.domain.Consultorio;
import uce.proyect.domain.Empleado;

/**
 *
 * @author Erick
 */
@Stateless
public class ConsultorioServiceImp implements ConsultorioService {

    @Inject
    private ConsultorioDao consultorioDao;

    @Override
    public List<Consultorio> buscarTodosConsultorios() {
        return consultorioDao.findAllConsultorios();
    }

    @Override
    public Consultorio buscarConsultorioPorId(Consultorio consultorio) {
        return consultorioDao.findConsultorioById(consultorio);
    }

    @Override
    public Consultorio buscarConsultorioPorCodigo(Consultorio consultorio) {
        return consultorioDao.findConsultorioByCodigo(consultorio);
    }

    @Override
    public Consultorio buscarConsultorioPorEspecialidad(Consultorio consultorio) {
        return consultorioDao.findConsultorioByEspecialidad(consultorio);
    }

    @Override
    public List<Consultorio> buscarConsultorioPorEstado(Consultorio consultorio) {
        return consultorioDao.findConsultorioByEstado(consultorio);
    }

    @Override
    public void guardarConsultorio(Consultorio consultorio) {
        consultorioDao.insertConsultorio(consultorio);
    }

    @Override
    public void actualizarConsultorio(Consultorio consultorio) {
        consultorioDao.updateConsultorio(consultorio);
    }

    @Override
    public void eliminarConsultorio(Consultorio consultorio) {
        consultorioDao.deleteConsultorio(consultorio);
    }

    @Override
    public List<Empleado> obtenerReporte(String especialidad) {
        return consultorioDao.generatedQuery(especialidad);
    }

}
