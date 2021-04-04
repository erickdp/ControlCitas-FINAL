package uce.proyect.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import uce.proyect.domain.DetalleHistorial;

@Stateless
public class DetalleHistorialImp implements DetalleHistorialDao {

    @PersistenceContext(name = "CitasPU")
    EntityManager em;

    @Override
    public List<DetalleHistorial> findAllDetalleHistorials() {
        return em.createNamedQuery("DetalleHistorial.findAll").getResultList();
    }

    @Override
    public DetalleHistorial findDetalleHistorialById(DetalleHistorial historialMedico) {
        return em.find(DetalleHistorial.class, historialMedico.getIdDetalleHistorial());
    }

    @Override
    public DetalleHistorial findDetalleHistorialByCodigo(DetalleHistorial historialMedico) {
        List<DetalleHistorial> list = em.createNamedQuery("DetalleHistorial.findByCodigoDetalle")
                .setParameter("codigoDetalle", historialMedico.getCodigoDetalle()).getResultList();
        if(!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void insertDetalleHistorial(DetalleHistorial historialMedico) {
        em.persist(historialMedico);
    }

    @Override
    public void updateDetalleHistorial(DetalleHistorial historialMedico) {
        em.merge(historialMedico);
    }

    @Override
    public void deleteDetalleHistorial(DetalleHistorial historialMedico) {
        em.remove(em.merge(historialMedico));
    }

}
