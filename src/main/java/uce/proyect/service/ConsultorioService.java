/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uce.proyect.service;

import java.util.List;
import javax.ejb.Local;
import uce.proyect.domain.Consultorio;
import uce.proyect.domain.Empleado;

/**
 *
 * @author Erick
 */
@Local
public interface ConsultorioService {

    public List<Consultorio> buscarTodosConsultorios();

    public Consultorio buscarConsultorioPorId(Consultorio consultorio);

    public Consultorio buscarConsultorioPorCodigo(Consultorio consultorio);

    public Consultorio buscarConsultorioPorEspecialidad(Consultorio consultorio);

    public List<Consultorio> buscarConsultorioPorEstado(Consultorio consultorio);

    public void guardarConsultorio(Consultorio consultorio);

    public void actualizarConsultorio(Consultorio consultorio);

    public void eliminarConsultorio(Consultorio consultorio);

    List<Empleado> obtenerReporte(String especialidad);

}
