/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uce.proyect.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import uce.proyect.dao.DetalleHistorialDao;
import uce.proyect.domain.DetalleHistorial;

/**
 *
 * @author Erick
 */
//No use
@Stateless
public class DetalleHistorialServiceImp implements DetalleHistorialService {

    @Inject
    private DetalleHistorialDao detalleHistorialDao;
    
    @Override
    public List<DetalleHistorial> buscarTodosDetalleHistorials() {
        return detalleHistorialDao.findAllDetalleHistorials();
    }

    @Override
    public DetalleHistorial buscarDetalleHistorialPorId(DetalleHistorial historialMedico) {
        return detalleHistorialDao.findDetalleHistorialById(historialMedico);
    }

    @Override
    public DetalleHistorial buscarDetalleHistorialPorCodigo(DetalleHistorial historialMedico) {
        return detalleHistorialDao.findDetalleHistorialByCodigo(historialMedico);
    }

    @Override
    public void guardarDetalleHistorial(DetalleHistorial historialMedico) {
        detalleHistorialDao.insertDetalleHistorial(historialMedico);
    }

    @Override
    public void actualizarDetalleHistorial(DetalleHistorial historialMedico) {
        detalleHistorialDao.updateDetalleHistorial(historialMedico);
    }

    @Override
    public void eliminarDetalleHistorial(DetalleHistorial historialMedico) {
        detalleHistorialDao.deleteDetalleHistorial(historialMedico);
    }
    
}
