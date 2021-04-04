/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uce.proyect.service;

import java.util.List;
import javax.ejb.Local;
import uce.proyect.domain.Medico;

/**
 *
 * @author Erick
 */
@Local
public interface MedicoService {
    
    public List<Medico> buscarTodosMedicos();

    public Medico buscarMedicoPorId(Medico medico);
    
    public void actualizarMedico(Medico medico);
    
}
