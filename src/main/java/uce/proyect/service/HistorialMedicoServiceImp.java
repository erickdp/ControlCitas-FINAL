package uce.proyect.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import uce.proyect.dao.HistorialDao;
import uce.proyect.domain.HistorialMedico;

/**
 *
 * @author Erick
*/
@Stateless
public class HistorialMedicoServiceImp implements HistorialMedicoService {
    
    @Inject
    private HistorialDao historialMedicoDao;

    @Override
    public List<HistorialMedico> buscarTodosHistorialMedicos() {
        return historialMedicoDao.findAllHistorialMedicos();
    }

    @Override
    public HistorialMedico buscarHistorialMedicoPorId(HistorialMedico historialMedico) {
        return historialMedicoDao.findHistorialMedicoById(historialMedico);
    }

    @Override
    public HistorialMedico buscarHistorialMedicoPorCodigo(HistorialMedico historialMedico) {
        return historialMedicoDao.findHistorialMedicoByCodigo(historialMedico);
    }

    @Override
    public void guardarHistorialMedico(HistorialMedico historialMedico) {
        historialMedicoDao.insertHistorialMedico(historialMedico);
    }

    @Override
    public void actualizarHistorialMedico(HistorialMedico historialMedico) {
        historialMedicoDao.updateHistorialMedico(historialMedico);
    }

    @Override
    public void eliminarHistorialMedico(HistorialMedico historialMedico) {
        historialMedicoDao.deleteHistorialMedico(historialMedico);
    }
    
}
