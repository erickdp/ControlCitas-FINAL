package uce.proyect.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import uce.proyect.domain.Consultorio;
import uce.proyect.domain.HorarioAtencion;

@Stateless
public class HorarioImp implements HorarioDao {

    @PersistenceContext(name = "CitasPU")
    EntityManager em;

    @Override
    public List<HorarioAtencion> findAllHorarios() {
        return em.createNamedQuery("HorarioAtencion.findAll").getResultList();
    }

    @Override
    public HorarioAtencion findHorarioById(HorarioAtencion horarioAtencion) {
        return (HorarioAtencion) em.createNamedQuery("HorarioAtencion.findByIdHorarioAtencion").
                setParameter("idHorarioAtencion", horarioAtencion.getIdHorarioAtencion()).getSingleResult();
    }

    @Override
    public void insertHorarioAtencion(HorarioAtencion horarioAtencion) {
        em.persist(horarioAtencion);
    }

    @Override
    public void deleteHorarioAtencion(HorarioAtencion horarioAtencion) {
        horarioAtencion.setEstado("INACTIVO");
        List<Consultorio> listaConsultorios = horarioAtencion.getConsultorioList();
        for (int i = 0; i < listaConsultorios.size(); i++) {
            listaConsultorios.get(i).setActivo("INACTIVO");
        }
        horarioAtencion.setConsultorioList(listaConsultorios);
        em.merge(horarioAtencion);
//        em.remove(em.merge(horarioAtencion));
    }

    @Override
    public void updateHorarioAtencion(HorarioAtencion horarioAtencion) {
        em.merge(horarioAtencion);
    }

    @Override
    public HorarioAtencion findHour(HorarioAtencion horarioAtencion) {
        List<HorarioAtencion> list = em.createNamedQuery("HorarioAtencion.findByHoraInicioAndHoraFinal")
                .setParameter("horaInicio", horarioAtencion.getHoraInicio())
                .setParameter("horaFinal", horarioAtencion.getHoraFinal()).getResultList();
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public HorarioAtencion findObject(HorarioAtencion horarioAtencion) {
        List<HorarioAtencion> list = em.createNamedQuery("HorarioAtencion.findObject")
                .setParameter("horaInicio", horarioAtencion.getHoraInicio())
                .setParameter("horaFinal", horarioAtencion.getHoraFinal())
                .setParameter("diasLaborales", horarioAtencion.getDiasLaborales())
                .getResultList();
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

}
