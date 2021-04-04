package uce.proyect.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import uce.proyect.domain.Usuario;

@Stateless
public class UsuarioDaoImp implements UsuarioDao {

    @PersistenceContext(name = "CitasPU")
    EntityManager em;

    @Override
    public List<Usuario> findAllUsuarios() {
        return em.createNamedQuery("Usuario.findAll").getResultList();
    }

    @Override
    public Usuario findUsuarioById(Usuario usuario) {
        return (Usuario) em.createNamedQuery("Usuario.findByIdUsuario").setParameter("idUsuario", usuario.getIdUsuario()).getSingleResult();
    }

    @Override
    public Usuario findUsuarioByTelefono(Usuario usuario) {
        return (Usuario) em.createNamedQuery("Usuario.findByTelefono").setParameter("telefono", usuario.getTelefono()).getSingleResult();
    }

    @Override
    public void insertUsuario(Usuario usuario) {
        em.persist(usuario);
    }

    @Override
    public void updateUsuario(Usuario usuario) {
        em.merge(usuario);
    }

    @Override
    public void deleteUsuario(Usuario usuario) {
        em.remove(em.merge(usuario));
    }

    @Override
    public Usuario iniciarSession(Usuario usuario) {
//        Cada vez que se cree un query se debe de hacer un clean and build de la apl
//          Usar listar para evitar problemas al tener single result
        List<Usuario> list = em.createNamedQuery("Usuario.findByNombreUsuarioAndContrasena")
                .setParameter("nombreUsuario", usuario.getNombreUsuario())
                .setParameter("contrasena", usuario.getContrasena()).getResultList();
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public Usuario findUsuarioByName(String nombreUsuario) {
        List<Usuario> list = em.createNamedQuery("Usuario.findUsuarioByName")
                .setParameter("nombreUsuario", nombreUsuario).getResultList();

        if (!list.isEmpty()) {
            return list.get(0);
        }
        
        return null;

    }

}
