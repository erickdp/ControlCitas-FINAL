/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uce.proyect.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import uce.proyect.dao.UsuarioDao;
import uce.proyect.domain.Usuario;

/**
 *
 * @author Erick
*/
@Stateless
public class UsuarioServiceImp implements UsuarioService{

    @Inject
    private UsuarioDao usuarioDao;
    
    @Override
    public List<Usuario> buscarTodosUsuarios() {
        return usuarioDao.findAllUsuarios();
    }

    @Override
    public Usuario buscarUsuarioPorId(Usuario usuario) {
        return usuarioDao.findUsuarioById(usuario);
    }

    @Override
    public Usuario buscarUsuarioPorTelefono(Usuario usuario) {
        return usuarioDao.findUsuarioByTelefono(usuario);
    }

    @Override
    public void guardarUsuario(Usuario usuario) {
        usuarioDao.insertUsuario(usuario);
    }

    @Override
    public void actualizarUsuario(Usuario usuario) {
        usuarioDao.updateUsuario(usuario);
    }

    @Override
    public void eliminarUsuario(Usuario usuario) {
        usuarioDao.deleteUsuario(usuario);
    }

    @Override
    public Usuario iniciarSesion(Usuario usuario) {
        return usuarioDao.iniciarSession(usuario);
    }

    @Override
    public Usuario buscarUsuarioPorName(String nombreUsuario) {
        return usuarioDao.findUsuarioByName(nombreUsuario);
    }
    
    
    
}
