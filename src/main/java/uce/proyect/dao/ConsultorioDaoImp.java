package uce.proyect.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import uce.proyect.domain.Consultorio;
import uce.proyect.domain.Empleado;

@Stateless
public class ConsultorioDaoImp implements ConsultorioDao {

    @PersistenceContext(name = "CitasPU")
    private EntityManager em;

    @Override
    public List<Consultorio> findAllConsultorios() {
        return em.createNamedQuery("Consultorio.findAll").getResultList();
    }

    @Override
    public Consultorio findConsultorioById(Consultorio consultorio) {
        return em.find(Consultorio.class, consultorio.getIdConsultorio());
    }

    @Override
    public Consultorio findConsultorioByCodigo(Consultorio consultorio) {
        List<Consultorio> list = em.createNamedQuery("Consultorio.findByCodConsultorio")
                .setParameter("codConsultorio", consultorio.getCodConsultorio()).getResultList();
        if(!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

//    Siempre usar resultlist no importa que devuelva un campo porque manda un error al usar single result
    @Override
    public Consultorio findConsultorioByEspecialidad(Consultorio consultorio) {
        List<Consultorio> list = em.createNamedQuery("Consultorio.findByEspecialidad")
                .setParameter("especialidad", consultorio.getEspecialidad()).getResultList();
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<Consultorio> findConsultorioByEstado(Consultorio consultorio) {
        return em.createNamedQuery("Consultorio.findByActivo").setParameter("activo", consultorio.getActivo()).getResultList();
    }

    @Override
    public void insertConsultorio(Consultorio consultorio) {
        em.persist(consultorio);
    }

    @Override
    public void updateConsultorio(Consultorio consultorio) {
        em.merge(consultorio);
    }

    @Override
    public void deleteConsultorio(Consultorio consultorio) {
        consultorio.setActivo("INACTIVO");
        em.merge(consultorio);
    }

    @Override
    public List<Empleado> generatedQuery(String param) {
        return em.createQuery("SELECT e FROM Empleado e JOIN e.medico m JOIN m.idConsultorio c WHERE c.especialidad = :param").
                setParameter("param", param).getResultList();
    }

}
