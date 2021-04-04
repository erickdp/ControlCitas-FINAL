package uce.proyect.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import uce.proyect.domain.HistorialMedico;

@Stateless
public class HistorialMedicoImp implements HistorialDao {

    @PersistenceContext(name = "CitasPU")
    EntityManager em;

    @Override
    public List<HistorialMedico> findAllHistorialMedicos() {
        return em.createNamedQuery("HistorialMedico.findAll").getResultList();
    }

    @Override
    public HistorialMedico findHistorialMedicoById(HistorialMedico historialMedico) {
        List<HistorialMedico> hm = em.createNamedQuery("HistorialMedico.findByIdUsuario")
                .setParameter(historialMedico.getIdUsuario(), "idUsuario").getResultList();
        if (!hm.isEmpty()) {
            return hm.get(0);
        }
        return null;
    }

    @Override
    public HistorialMedico findHistorialMedicoByCodigo(HistorialMedico historialMedico) {
        return (HistorialMedico) em.createNamedQuery("HistorialMedico.findByCodigoHistorial")
                .setParameter("codigoHistorial", historialMedico.getCodigoHistorial()).getSingleResult();
    }

    @Override
    public void insertHistorialMedico(HistorialMedico historialMedico) {
        em.persist(historialMedico);
    }

    @Override
    public void updateHistorialMedico(HistorialMedico historialMedico) {
        em.merge(historialMedico);
    }

    @Override
    public void deleteHistorialMedico(HistorialMedico historialMedico) {
        em.remove(em.merge(historialMedico));
    }

}
