package uce.proyect.web;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import uce.proyect.domain.Cita;
import uce.proyect.domain.Usuario;
import uce.proyect.service.UsuarioService;

@Getter
@Setter
@Named
@RequestScoped
public class UsuarioSesionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private UsuarioService usuarioService;

    private Usuario usuarioSesion;

    private List<Cita> citasList;

    @PostConstruct
    public void init() {
        usuarioSesion = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .get("usuario");
        Usuario p = usuarioService.buscarUsuarioPorId(usuarioSesion);
        citasList = p.getPaciente().getCitaList();
   }


}
