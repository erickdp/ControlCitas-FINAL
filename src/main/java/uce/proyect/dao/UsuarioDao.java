package uce.proyect.dao;

import java.util.List;
import uce.proyect.domain.Usuario;

public interface UsuarioDao {
    
    public List<Usuario> findAllUsuarios();

    public Usuario findUsuarioById(Usuario usuario);

    public Usuario findUsuarioByTelefono(Usuario usuario);

    public void insertUsuario(Usuario usuario);

    public void updateUsuario(Usuario usuario);

    public void deleteUsuario(Usuario usuario);
    
    public Usuario iniciarSession(Usuario usuario);
    
    public Usuario findUsuarioByName(String nombreUsuario);
    
}
