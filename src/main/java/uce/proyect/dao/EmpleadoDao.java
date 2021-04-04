package uce.proyect.dao;

import java.util.List;
import javax.ejb.Local;
import uce.proyect.domain.Empleado;

public interface EmpleadoDao {

    public List<Empleado> findAllEmpleados();

    public Empleado findEmpleadoById(Empleado empleado);

    public Empleado findEmpleadoByCi(Empleado empleado);
    
    public List<Empleado> findEmpleadoByCargo(Empleado empleado);

    public List<Empleado> findEmpleadoByEstado(Empleado empleado);

    public void insertEmpleado(Empleado empleado);

    public void updateEmpleado(Empleado empleado);

    public void deleteEmpleado(Empleado empleado);
    
}
