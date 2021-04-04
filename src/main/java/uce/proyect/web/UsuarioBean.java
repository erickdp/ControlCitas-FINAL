package uce.proyect.web;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import uce.proyect.domain.Usuario;
import uce.proyect.service.UsuarioService;

@Getter
@Setter
@Named
@SessionScoped
public class UsuarioBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private UsuarioService usuarioService;

    private Usuario usuarioLogin;

    private Usuario usuarioSesion;

    @PostConstruct
    public void init() {
        this.usuarioLogin = new Usuario();
    }

    public String ingresar() {
        String mensaje = "Las credenciales son inocrrectas";
        this.usuarioLogin = usuarioService.iniciarSesion(this.usuarioLogin);
        if (this.usuarioLogin != null) {
            usuarioSesion = this.usuarioLogin;

            this.usuarioLogin = new Usuario();
//            Almacenar la sesion del usuario en facesContext

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", usuarioSesion);
            if (usuarioSesion.getEmpleado() != null && usuarioSesion.getEmpleado().getActivo().equalsIgnoreCase("ACTIVO")) {
                return "pages/empleadoPerfil?faces-redirect=true";
            } else if (usuarioSesion.getEmpleado() == null && usuarioSesion.getPaciente().getActivo().equalsIgnoreCase("ACTIVO")) {
                return "pages/pacientePerfil?faces-redirect=true";
            }
            mensaje = "Usuario inhabilitado, pongace en contacto con el administrador. Inf: erickdp@hotmail.com";
        }
        this.usuarioLogin = new Usuario();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
                "Aviso", mensaje));
        return null;
    }

    public void cerrarSesion() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        this.usuarioSesion = new Usuario();
    }

    public boolean validarOpcion() {
        return this.usuarioSesion.getEmpleado().getCargo().equalsIgnoreCase("ADMINISTRADOR");
    }

    public boolean validarOpcionGestion() {
        return this.usuarioSesion.getEmpleado().getCargo().equalsIgnoreCase("ADMINISTRADOR")
                || this.usuarioSesion.getEmpleado().getCargo().equalsIgnoreCase("SECRETARIO");
    }

    public boolean validarOpcionMedico() {
        return this.usuarioSesion.getEmpleado().getCargo().equalsIgnoreCase("MEDICO");
    }

    public String accesoCitas() {
        if (this.usuarioSesion.getEmpleado().getMedico() != null) {
            return "/pages/subpages/citasAtencion?faces-redirect=true";
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No puede acceder", "Espere a ser agreado a un consultorio para habilitar sus citas, contactece con algún adminsitrador."
                + "Inf: erickdp@hotmail.com"));
        PrimeFaces.current().ajax().update(":menuBar:msgsSticky");
        return null;
    }
}
