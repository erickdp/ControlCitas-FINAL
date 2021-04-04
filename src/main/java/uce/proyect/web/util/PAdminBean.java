package uce.proyect.web.util;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Random;
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
import uce.proyect.domain.HistorialMedico;
import uce.proyect.domain.Paciente;
import uce.proyect.domain.Usuario;
import uce.proyect.service.PacienteService;
import uce.proyect.service.UsuarioService;

@Getter
@Setter
@Named("paAdminBean")
@ViewScoped
public class PAdminBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private PacienteService pacienteService;

    @Inject
    private UsuarioService usuarioService;

//    private Usuario usuarioSesion;
    private List<Paciente> pacienteList;

    private Paciente pacienteSelect;

    private Usuario usuarioSelect;

    private String fechaNacimiento;

    @PostConstruct
    public void init() {
//        No es necesario en este momento pero se puede usar para agregar un boton en la barra de menu
//        this.usuarioSesion = (Usuario) FacesContext.getCurrentInstance()
//                .getExternalContext().getSessionMap().get("usuario");

        this.pacienteList = this.pacienteService.buscarTodosPacientes();
        this.pacienteSelect = new Paciente();
        this.usuarioSelect = new Usuario();
        this.fechaNacimiento = "";
    }

    public void eliminarPaciente() {
        try {
            this.pacienteService.eliminarPaciente(this.pacienteSelect);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Eliminado", "Paciente Eliminado, tener en cuenta que sus citas cambiaran a modo inhabilitado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al eliminar"));
        } finally {
            PrimeFaces.current().ajax().update(":menuBar:msgs");
        }
    }

    public void habilitarPaciente() {
        try {
            this.pacienteSelect.setActivo("ACTIVO");
            this.pacienteService.actuaizarPaciente(this.pacienteSelect);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Habilitado", "Paciente Habilitado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al eliminar"));
        } finally {
            PrimeFaces.current().ajax().update(":menuBar:msgs");
        }
    }

    public void onRowEdit(RowEditEvent<Paciente> event) {

        try {

            Paciente pacienteEdit = event.getObject();

            this.pacienteService.actuaizarPaciente(pacienteEdit);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualizado", "Paciente Actualizado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo actualizar"));
        }
    }

    /*
    Para agregar un objeto y que se guarde en cascada se debe de instanciar todos los objetos relacionados
    a el, pues no los genera de manera automatica y por tanto salta un error
     */
    public void agregarPaciente() {
        try {
            HistorialMedico hm = new HistorialMedico();
            hm.setFechaApertura(new Date());
            hm.setTipoSangre("N/A");
            Random rn = new Random();
            String codigo = "";
            for (int i = 0; i < 9; i++) {
                codigo += (char) (rn.nextInt(26) + 'A');
            }
            validarEmail(this.usuarioSelect.getMail());
//            Le doy el date parseado
            this.pacienteSelect.setPFechaNacimiento(convertirFecha(this.fechaNacimiento));
            hm.setCodigoHistorial(codigo);
//            hm.setDetalleHistorialList(new ArrayList<>());
//            Este metodo falta anadirle el historial medico y el detalle pero el admin no puede hacer esto porque
//                    necesita por ejemplo el tipo de sangre, esto podrian hacer enfermeras y medicos
            this.usuarioService.guardarUsuario(this.usuarioSelect);
            //Hago esto porque el no tiene autoincremental en la llave primaria
            this.usuarioSelect = this.usuarioService.buscarUsuarioPorName(this.usuarioSelect.getNombreUsuario());
            int idUsuario = this.usuarioSelect.getIdUsuario();
            this.pacienteSelect.setActivo("ACTIVO");
            this.pacienteSelect.setIdUsuario(idUsuario);
//            Validaciones
            this.pacienteSelect.setPApellido(this.pacienteSelect.getPApellido().toUpperCase());
            this.pacienteSelect.setPNombre(this.pacienteSelect.getPNombre().toUpperCase());
            this.pacienteSelect.setPGenero(this.pacienteSelect.getPGenero().toUpperCase());
//            Fin
            hm.setIdUsuario(idUsuario);
            this.pacienteSelect.setHistorialMedico(hm);
            this.usuarioSelect.setPaciente(this.pacienteSelect);
            this.pacienteSelect.setUsuario(this.usuarioSelect);
//            this.pacienteService.insertPaciente(this.pacienteSelect);
            this.usuarioService.actualizarUsuario(this.usuarioSelect);
            this.pacienteService.actuaizarPaciente(this.pacienteSelect);

            this.pacienteSelect = new Paciente();
            this.usuarioSelect = new Usuario();

            this.pacienteList = this.pacienteService.buscarTodosPacientes();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Agregado"));
            PrimeFaces.current().executeScript("PF('dlgAgregar').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
            e.printStackTrace(System.out);
        } finally {
            PrimeFaces.current().ajax().update(":menuBar:msgs");
        }
    }

//    Como uso un inputmask en la vista entonces ese solo acepta string y por tanto debo castearlo
    public Date convertirFecha(String cadena) throws Exception {
        try {
            LocalDateTime fecha = LocalDateTime.of(LocalDate.parse(cadena), LocalTime.now());
            this.fechaNacimiento = "";
            return java.sql.Timestamp.valueOf(fecha);
        } catch (DateTimeParseException e) {
            throw new Exception("Error en fecha");
        }
    }

    public void validarEmail(String email) throws Exception {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if (!email.matches(regex)) {
            throw new Exception("El email es incorrecto");
        }
    }
}
