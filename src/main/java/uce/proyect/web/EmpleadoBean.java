package uce.proyect.web;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import uce.proyect.domain.Consultorio;
import uce.proyect.domain.Empleado;
import uce.proyect.domain.Paciente;
import uce.proyect.domain.Usuario;
import uce.proyect.service.UsuarioService;

@Getter
@Setter
@Named
@ViewScoped
public class EmpleadoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private UsuarioService usuarioService;

    private List<Paciente> pacientesList;

    private List<Empleado> empleadosList;

    private List<Consultorio> consultoriosList;

    private Usuario usuarioSelect;

    private Usuario usuarioSesion;

    private String identificador;

    private boolean identificadorSelec;

    @PostConstruct
    public void init() {
        this.usuarioSesion = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
    }

    public void actualizarUsuario() {

        try {
            Empleado empleadoUpper = this.usuarioSelect.getEmpleado();
            empleadoUpper.setNombre(empleadoUpper.getNombre().toUpperCase());
            empleadoUpper.setApellido(empleadoUpper.getApellido().toUpperCase());
            this.usuarioSelect.setNombreUsuario(this.usuarioSelect.getNombreUsuario().trim());
            this.usuarioService.actualizarUsuario(this.usuarioSelect);
            this.usuarioSesion = this.usuarioService.buscarUsuarioPorName(this.usuarioSelect.getNombreUsuario());
            this.usuarioSelect = new Usuario();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Aviso", "Se ha actualizado correctamente el empleado"));
            PrimeFaces.current().executeScript("PF('dlgEmpleado').hide();");
            PrimeFaces.current().ajax().update(":formPerfil:panelGrid");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL,
                            "Aviso", "No se ha actualizado correctamente el empleado"));
        } finally {
            PrimeFaces.current().ajax().update(":menuBar:msgs");
        }
    }

    public boolean cargarOpcion() {
        return !this.usuarioSesion.getEmpleado().getCargo().equalsIgnoreCase("ADMINISTRADOR");
    }

}
