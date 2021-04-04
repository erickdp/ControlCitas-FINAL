/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uce.proyect.service;

import java.util.List;
import javax.ejb.Local;
import uce.proyect.domain.DetalleHistorial;

/**
 *
 * @author Erick
 */
@Local
public interface DetalleHistorialService {
    
    public List<DetalleHistorial> buscarTodosDetalleHistorials();

    public DetalleHistorial buscarDetalleHistorialPorId(DetalleHistorial historialMedico);

    public DetalleHistorial buscarDetalleHistorialPorCodigo(DetalleHistorial historialMedico);

    public void guardarDetalleHistorial(DetalleHistorial historialMedico);

    public void actualizarDetalleHistorial(DetalleHistorial historialMedico);

    public void eliminarDetalleHistorial(DetalleHistorial historialMedico);
    
}
