/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uce.proyect.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import uce.proyect.dao.EmpleadoDao;
import uce.proyect.domain.Empleado;

/**
 *
 * @author Erick
 */
@Stateless
public class EmpleadoServiceImp implements EmpleadoService {

    @Inject
    private EmpleadoDao empleadoDao;

    @Override
    public List<Empleado> buscarTodosEmpleados() {
        return empleadoDao.findAllEmpleados();
    }

    @Override
    public Empleado buscarEmpleadoPorId(Empleado empleado) {
        return empleadoDao.findEmpleadoById(empleado);
    }

    @Override
    public Empleado buscarEmpleadoPorCi(Empleado empleado) {
        return empleadoDao.findEmpleadoByCi(empleado);
    }

    @Override
    public List<Empleado> buscarEmpleadoPorCargo(Empleado empleado) {
        return empleadoDao.findEmpleadoByCargo(empleado);
    }

    @Override
    public List<Empleado> buscarEmpleadoPorEstado(Empleado empleado) {
        return empleadoDao.findEmpleadoByEstado(empleado);
    }

    @Override
    public void guardarEmpleado(Empleado empleado) {
        empleadoDao.insertEmpleado(empleado);
    }

    @Override
    public void actualizarEmpleado(Empleado empleado) {
        empleadoDao.updateEmpleado(empleado);
    }

    @Override
    public void eliminarEmpleado(Empleado empleado) {
        empleadoDao.deleteEmpleado(empleado);
    }

}
