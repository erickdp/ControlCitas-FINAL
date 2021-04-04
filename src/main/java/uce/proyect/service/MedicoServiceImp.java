/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uce.proyect.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import uce.proyect.dao.MedicoDao;
import uce.proyect.domain.Medico;

/**
 *
 * @author Erick
 */
@Stateless
public class MedicoServiceImp implements MedicoService {

    @Inject
    private MedicoDao medicoDao;

    @Override
    public List<Medico> buscarTodosMedicos() {
        return medicoDao.findAllMedicos();
    }

    @Override
    public Medico buscarMedicoPorId(Medico medico) {
        return medicoDao.findMedicoById(medico);
    }

    @Override
    public void actualizarMedico(Medico medico) {
        medicoDao.updateMedico(medico);
    }
    
    
}
