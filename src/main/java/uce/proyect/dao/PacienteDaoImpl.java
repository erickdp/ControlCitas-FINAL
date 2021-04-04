package uce.proyect.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import uce.proyect.domain.Paciente;

@Stateless
public class PacienteDaoImpl implements PacienteDao {

    @PersistenceContext(name = "CitasPU")
    EntityManager em;

    @Override
    public List<Paciente> findAllPacientes() {
        return em.createNamedQuery("Paciente.findAll").getResultList();
    }

    @Override
    public Paciente findPacienteById(Paciente paciente) {
        List<Paciente> list = em.createNamedQuery("Paciente.findByIdUsuario")
                .setParameter("idUsuario", paciente.getIdUsuario()).getResultList();
        if(!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public Paciente findPacienteByCi(Paciente paciente) {
        return (Paciente) em.createNamedQuery("Paciente.findByPCi").setParameter("pCi", paciente.getPCi()).getSingleResult();
    }

    @Override
    public List<Paciente> findPacienteByEstado(Paciente paciente) {
        return em.createNamedQuery("Paciente.findByActivo").setParameter("activo", paciente.getActivo()).getResultList();
    }

    @Override
    public void insertPaciente(Paciente paciente) {
        em.persist(paciente);
    }

    @Override
    public void updatePaciente(Paciente paciente) {
        em.merge(paciente);
    }

    @Override
    public void deletePaciente(Paciente paciente) {
        paciente.setActivo("INACTIVO"); //Estado inactivo
        em.merge(paciente);
    }

}
