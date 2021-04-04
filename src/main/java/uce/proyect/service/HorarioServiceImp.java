/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uce.proyect.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import uce.proyect.dao.HorarioDao;
import uce.proyect.domain.HorarioAtencion;

/**
 *
 * @author Erick
 */
@Stateless
public class HorarioServiceImp implements HorarioService {

    @Inject
    private HorarioDao horarioDao;
    
    @Override
    public List<HorarioAtencion> buscarTodosHorarios() {
        return horarioDao.findAllHorarios();
    }

    @Override
    public HorarioAtencion buscarHorarioPorId(HorarioAtencion horarioAtencion) {
        return horarioDao.findHorarioById(horarioAtencion);
    }

    @Override
    public void insertarHorarioAtencion(HorarioAtencion horarioAtencion) {
        horarioDao.insertHorarioAtencion(horarioAtencion);
    }

    @Override
    public void eliminarHorarioAtencion(HorarioAtencion horarioAtencion) {
        horarioDao.deleteHorarioAtencion(horarioAtencion);
    }

    @Override
    public void actualizarHorarioAtencion(HorarioAtencion horarioAtencion) {
        horarioDao.updateHorarioAtencion(horarioAtencion);
    }

    @Override
    public HorarioAtencion buscarHorario(HorarioAtencion horarioAtencion) {
        return horarioDao.findHour(horarioAtencion);
    }

    @Override
    public HorarioAtencion buscarHorarioCompleto(HorarioAtencion horarioAtencion) {
        return horarioDao.findObject(horarioAtencion);
    }
    
}
