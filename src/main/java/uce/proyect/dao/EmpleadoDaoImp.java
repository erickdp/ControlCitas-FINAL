package uce.proyect.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import uce.proyect.domain.Empleado;

@Stateless
public class EmpleadoDaoImp implements EmpleadoDao {

    @PersistenceContext(name = "CitasPu")
    EntityManager em;
    
    @Override
    public List<Empleado> findAllEmpleados() {
        return em.createNamedQuery("Empleado.findAll").getResultList();
    }

    @Override
    public Empleado findEmpleadoById(Empleado empleado) {
        return em.find(Empleado.class, empleado.getIdUsuario());
    }

    @Override
    public Empleado findEmpleadoByCi(Empleado empleado) {
        return (Empleado) em.createNamedQuery("Empleado.findByCi")
                .setParameter("ci", empleado.getCi()).getSingleResult();
    }

    @Override
    public List<Empleado> findEmpleadoByEstado(Empleado empleado) {
        return em.createNamedQuery("Empleado.findByActivo")
                .setParameter("activo", empleado.getActivo()).getResultList();
    }

    @Override
    public void insertEmpleado(Empleado empleado) {
        em.persist(empleado);
    }

    @Override
    public void updateEmpleado(Empleado empleado) {
        em.merge(empleado);
    }

    @Override
    public void deleteEmpleado(Empleado empleado) {
        empleado.setActivo("INACTIVO");
        em.merge(empleado);
    }

    @Override
    public List<Empleado> findEmpleadoByCargo(Empleado empleado) {
        return em.createNamedQuery("Empleado.findByCargo").setParameter("cargo", empleado.getCargo()).getResultList();
    }

}
