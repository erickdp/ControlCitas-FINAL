/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uce.proyect.service;

import java.util.List;
import javax.ejb.Local;
import uce.proyect.domain.Usuario;

/**
 *
 * @author Erick
 */
@Local
public interface UsuarioService {
    
    public List<Usuario> buscarTodosUsuarios();

    public Usuario buscarUsuarioPorId(Usuario usuario);

    public Usuario buscarUsuarioPorTelefono(Usuario usuario);

    public void guardarUsuario(Usuario usuario);

    public void actualizarUsuario(Usuario usuario);

    public void eliminarUsuario(Usuario usuario);
    
    public Usuario iniciarSesion(Usuario usuario);
    
    public Usuario buscarUsuarioPorName(String nombreUsuario);
    
}
