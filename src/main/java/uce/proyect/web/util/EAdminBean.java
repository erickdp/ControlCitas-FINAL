package uce.proyect.web.util;

//Clase para administrar la tabla de empleados
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
import org.primefaces.event.RowEditEvent;
import uce.proyect.domain.Empleado;
import uce.proyect.domain.Usuario;
import uce.proyect.service.EmpleadoService;
import uce.proyect.service.UsuarioService;

//Clase terminada tener en cuenta si se usa un boton par eliminar
@Getter
@Setter
@Named("eAdminBean")
@ViewScoped
public class EAdminBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EmpleadoService empleadoService;

    @Inject
    private UsuarioService usuarioService;

    private List<Empleado> empleadoList;

    private Empleado empleadoSelect;

    private Usuario usuarioSelect;

    private String ci;

    @PostConstruct
    public void init() {
        this.empleadoList = empleadoService.buscarTodosEmpleados();
        this.empleadoSelect = new Empleado();
        this.usuarioSelect = new Usuario();
    }

    public void validarCedula() {
        System.out.println(ci);
        for (Empleado empleado : empleadoList) {
            if (empleado.getCi().equals(this.ci)) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_FATAL, "Errror", "Ci duplicado"));
                break;
            }
        }
    }

    public void onRowEdit(RowEditEvent<Empleado> event) {

        Empleado empleadoGuardar = event.getObject();
        empleadoGuardar.setNombre(empleadoGuardar.getNombre().toUpperCase());
        empleadoGuardar.setApellido(empleadoGuardar.getApellido().toUpperCase());

        this.empleadoService.actualizarEmpleado(empleadoGuardar);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualizacion", "Actualización Correcta"));
    }

    public void inhabilitarEmpleado() {
        this.empleadoService.eliminarEmpleado(this.empleadoSelect);
        this.empleadoSelect = new Empleado();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Actualizado", "Se inhabilito al empleado"));
        PrimeFaces.current().ajax().update(":menuBar:msgs");
    }

    public void habilitarEmpleado() {
        this.empleadoSelect.setActivo("ACTIVO");
        this.empleadoService.actualizarEmpleado(this.empleadoSelect);
        this.empleadoSelect = new Empleado();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Actualizado", "Se habilito al empleado"));
        PrimeFaces.current().ajax().update(":menuBar:msgs");
    }

    public void agregarEmpleado() {
        try {
            /*
            Se debe de guardar la tabla paadre la que proporciona el id para que las tablas que heredan
            este id se actualicen en cascada, en este caso de guardar el usuario para que se guarden
            en cascada sus demas valores en las relaciones con las tablas
             */
            validarEmail(this.usuarioSelect.getMail());
            //Acordarse de agregar una validacion para los consultorios
            this.usuarioService.guardarUsuario(this.usuarioSelect);
            this.usuarioSelect = this.usuarioService.buscarUsuarioPorName(this.usuarioSelect.getNombreUsuario());
            int idUsuario = this.usuarioSelect.getIdUsuario();
            this.empleadoSelect.setIdUsuario(idUsuario);
            this.empleadoSelect.setActivo("ACTIVO");
            this.empleadoSelect.setNombre(this.empleadoSelect.getNombre().toUpperCase());
            this.empleadoSelect.setApellido(this.empleadoSelect.getApellido().toUpperCase());

            this.usuarioSelect.setEmpleado(this.empleadoSelect);
            this.empleadoSelect.setUsuario(this.usuarioSelect);

//            Para solucionar el error de las PK se debe realizar una actualizacion de las 2 entidades
            this.usuarioService.actualizarUsuario(this.usuarioSelect);
            this.empleadoService.actualizarEmpleado(this.empleadoSelect);

            this.usuarioSelect = new Usuario();
            this.empleadoSelect = new Empleado();

            this.empleadoList = this.empleadoService.buscarTodosEmpleados();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualizacion", "Agregado Correctamente"));
            PrimeFaces.current().executeScript("PF('dlgAgregarEmpl').hide();");

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Actualizacion", e.getMessage()));
        } finally {
            PrimeFaces.current().ajax().update(":menuBar:msgs");
        }
    }

    public void validarEmail(String email) throws Exception {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if (!email.matches(regex)) {
            throw new Exception("El email es incorrecto");
        }
    }

    public void validarMedico(String contraseña) throws Exception {

        if (!contraseña.equals("MEDICO")) {
            throw new Exception("Error de credencial, contactece con el administrador");
        }
    }

//    Mirar como resetear los input en primefaces, no mandar nuevamente al inicio, consume mucho recurso
    public void agregarMedico() {

        try {
            validarEmail(this.usuarioSelect.getMail());
            validarMedico(this.empleadoSelect.getCargo());

            this.usuarioService.guardarUsuario(this.usuarioSelect);
            this.usuarioSelect = this.usuarioService.buscarUsuarioPorName(this.usuarioSelect.getNombreUsuario());

            int idUsuario = this.usuarioSelect.getIdUsuario();
            this.empleadoSelect.setIdUsuario(idUsuario);
            this.empleadoSelect.setActivo("ACTIVO");
            this.empleadoSelect.setNombre(this.empleadoSelect.getNombre().toUpperCase());
            this.empleadoSelect.setApellido(this.empleadoSelect.getApellido().toUpperCase());
            this.usuarioSelect.setEmpleado(this.empleadoSelect);
            this.empleadoSelect.setUsuario(this.usuarioSelect);

//            Para solucionar el error de las PK se debe realizar una actualizacion de las 2 entidades
            this.usuarioService.actualizarUsuario(this.usuarioSelect);
            this.empleadoService.actualizarEmpleado(this.empleadoSelect);

            this.usuarioSelect = new Usuario();
            this.empleadoSelect = new Empleado();

            this.empleadoList = this.empleadoService.buscarTodosEmpleados();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualizacion", "Agregado Correctamente"));
            PrimeFaces.current().executeScript("PF('dlgAgregarEmpl').hide();");

        } catch (Exception e) {
//            this.empleadoSelect = new Empleado();
//            this.usuarioSelect = new Usuario();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Actualizacion", e.getMessage()));
        } finally {
            PrimeFaces.current().ajax().update(":menuBar:msgs");
        }
    }

}
