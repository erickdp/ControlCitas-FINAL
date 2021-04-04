/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uce.proyect.service;

import java.util.List;
import javax.ejb.Local;
import uce.proyect.domain.Empleado;

/**
 *
 * @author Erick
 */
@Local
public interface EmpleadoService {
    
    public List<Empleado> buscarTodosEmpleados();

    public Empleado buscarEmpleadoPorId(Empleado empleado);

    public Empleado buscarEmpleadoPorCi(Empleado empleado);
    
    public List<Empleado> buscarEmpleadoPorCargo(Empleado empleado);

    public List<Empleado> buscarEmpleadoPorEstado(Empleado empleado);

    public void guardarEmpleado(Empleado empleado);

    public void actualizarEmpleado(Empleado empleado);

    public void eliminarEmpleado(Empleado empleado);
    
}
