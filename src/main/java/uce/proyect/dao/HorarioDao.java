package uce.proyect.dao;

import java.util.List;
import uce.proyect.domain.HorarioAtencion;

public interface HorarioDao {

    public List<HorarioAtencion> findAllHorarios();

    public HorarioAtencion findHorarioById(HorarioAtencion horarioAtencion);

    public void insertHorarioAtencion(HorarioAtencion horarioAtencion);

    public void deleteHorarioAtencion(HorarioAtencion horarioAtencion);

    public void updateHorarioAtencion(HorarioAtencion horarioAtencion);
    
    public HorarioAtencion findHour(HorarioAtencion horarioAtencion);
    
    public HorarioAtencion findObject(HorarioAtencion horarioAtencion);

}
