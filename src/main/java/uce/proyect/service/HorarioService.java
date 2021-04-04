/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uce.proyect.service;

import java.util.List;
import javax.ejb.Local;
import uce.proyect.domain.HorarioAtencion;

/**
 *
 * @author Erick
 */
@Local
public interface HorarioService {

    public List<HorarioAtencion> buscarTodosHorarios();

    public HorarioAtencion buscarHorarioPorId(HorarioAtencion horarioAtencion);

    public void insertarHorarioAtencion(HorarioAtencion horarioAtencion);

    public void eliminarHorarioAtencion(HorarioAtencion horarioAtencion);

    public void actualizarHorarioAtencion(HorarioAtencion horarioAtencion);
    
    public HorarioAtencion buscarHorario(HorarioAtencion horarioAtencion);
    
    public HorarioAtencion buscarHorarioCompleto(HorarioAtencion horarioAtencion);

}
