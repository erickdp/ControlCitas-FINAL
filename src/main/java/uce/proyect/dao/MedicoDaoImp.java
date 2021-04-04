package uce.proyect.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import uce.proyect.domain.Medico;

@Stateless
public class MedicoDaoImp implements MedicoDao {

    @PersistenceContext(name = "CitasPU")
    private EntityManager em;

    @Override
    public List<Medico> findAllMedicos() {
        return em.createNamedQuery("Medico.findAll").getResultList();
    }

    @Override
    public Medico findMedicoById(Medico medico) {
        return (Medico) em.createNamedQuery("Medico.findByIdUsuario").setParameter("idUsuario", medico.getIdUsuario());
    }

    @Override
    public void updateMedico(Medico medico) {
        em.merge(medico);
    }

}
