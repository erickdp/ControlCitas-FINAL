package uce.proyect.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import uce.proyect.domain.Cita;

@Stateless
public class CitaDaoImp implements CitaDao {

    @PersistenceContext(name = "CitasPU")
    private EntityManager em;

    @Override
    public List<Cita> findAllCitas() {
        return em.createNamedQuery("Cita.findAll").getResultList();
    }

    @Override
    public Cita findCitaById(Cita cita) {
        List<Cita> list = em.createNamedQuery("Cita.findByIdCita").setParameter("idCita", cita.getIdCita()).getResultList();
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public Cita findCitaByCodigo(Cita cita) {
        List<Cita> list = em.createNamedQuery("Cita.findByCodigoCita").
                setParameter("codigoCita", cita.getCodigoCita()).getResultList();
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<Cita> findCitaByEstado(Cita cita) {
        return em.createNamedQuery("Cita.findByActivo")
                .setParameter("activo", cita.getActivo()).getResultList();
    }

    @Override
    public void insertCita(Cita cita) {
        em.persist(cita);
    }

    @Override
    public void updateCita(Cita cita) {
        em.merge(cita);
    }

    @Override
    public void deleteCita(Cita cita) {
        cita.setActivo("INACTIVO");
        cita.setCompletado("NO_ATENDIDO");
        em.merge(cita);
    }

    @Override
    public Cita findByfechaCitaAndPaciente(Cita cita) {
        List<Cita> list = em.createNamedQuery("Cita.findByfechaCitaAndPaciente")
                .setParameter("fechaCita", cita.getFechaCita())
                .setParameter("idPaciente", cita.getIdUsuario()).getResultList();
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

}
