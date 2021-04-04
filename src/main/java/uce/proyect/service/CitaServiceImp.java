/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uce.proyect.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import uce.proyect.dao.CitaDao;
import uce.proyect.domain.Cita;

/**
 *
 * @author Erick
 */
@Stateless
public class CitaServiceImp implements CitaService {

    @Inject
    private CitaDao citaDao;
    
    @Override
    public List<Cita> buscarTodasCitas() {
        return citaDao.findAllCitas();
    }

    @Override
    public Cita buscarCitaPorId(Cita cita) {
        return citaDao.findCitaById(cita);
    }

    @Override
    public Cita buscarCitaPorCodigo(Cita cita) {
        return citaDao.findCitaByCodigo(cita);
    }

    @Override
    public List<Cita> buscarCitaPorEstado(Cita cita) {
        return citaDao.findCitaByEstado(cita);
    }

    @Override
    public void guardarCita(Cita cita) {
        citaDao.insertCita(cita);
    }

    @Override
    public void actualizarCita(Cita cita) {
        citaDao.updateCita(cita);
    }

    @Override
    public void eliminarCita(Cita cita) {
        citaDao.deleteCita(cita);
    }

    @Override
    public Cita buscarCitasConcurrentes(Cita cita) {
        return citaDao.findByfechaCitaAndPaciente(cita);
    }
    
    
}
