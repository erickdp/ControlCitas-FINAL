package uce.proyect.web;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import uce.proyect.domain.Cita;
import uce.proyect.domain.DetalleHistorial;
import uce.proyect.domain.HistorialMedico;
import uce.proyect.domain.Paciente;
import uce.proyect.domain.Usuario;
import uce.proyect.service.CitaService;
import uce.proyect.service.HistorialMedicoService;
import uce.proyect.service.UsuarioService;

@Getter
@Setter
@Named
@ViewScoped
public class MedicoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private CitaService citaService;

    @Inject
    private HistorialMedicoService historialService;

    @Inject
    private UsuarioService usuarioService;

    private Cita citaSelect;

    private List<Cita> citasList;

    private Usuario usuarioSesion;

    private ScheduleModel eventModel;

    private HistorialMedico historialMSelec;

    private List<DetalleHistorial> detalleHistorial;

    private DetalleHistorial detalleHistorialUpdate;

    private ScheduleEvent<?> event = new DefaultScheduleEvent<>();

    private String idCita;

    private int idUsuario;

    @PostConstruct
    public void init() {
        this.eventModel = new DefaultScheduleModel();

        this.usuarioSesion = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");

        this.detalleHistorialUpdate = new DetalleHistorial();

        cargarCalendario();

    }

//    Carga las citas para el medico solo se muestran citas activas
    public void cargarCalendario() {

        Usuario usuarioMedico = this.usuarioService.buscarUsuarioPorId(this.usuarioSesion);

        this.citasList = usuarioMedico.getEmpleado().getMedico().getCitaList();

        this.eventModel = new DefaultScheduleModel();

        DefaultScheduleEvent<?> evento;

        LocalDateTime fechaInicio;
        
        for (Cita cita : this.citasList) {

            if (cita.getActivo().equalsIgnoreCase("ACTIVO")) {

                fechaInicio = cita.getFechaCita().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                
                Paciente pacienteCita = cita.getIdUsuario();
                
                String estadoCita = pacienteCita.getActivo().equalsIgnoreCase("ACTIVO") ? "#11BB22" : "#f00";
                
//            Validar las fechas activas 
                evento = DefaultScheduleEvent.builder()
                        .title("Consulta: " + pacienteCita.getPNombre() + " " + pacienteCita.getPApellido())
                        .startDate(fechaInicio)
                        .endDate(fechaInicio.plusHours(1))
                        .overlapAllowed(true)
                        .description(String.valueOf(cita.getIdCita()))
                        .borderColor(estadoCita)
                        .build();
                eventModel.addEvent(evento);
            }
        }
    }
    
    public void cancelarCita() {
        Cita citaBuscar = new Cita();
        citaBuscar.setIdCita(Integer.parseInt(this.idCita));
        this.citaSelect = this.citaService.buscarCitaPorId(citaBuscar);
        
        Paciente pacienteCita = this.citaSelect.getIdUsuario();

        this.historialMSelec = pacienteCita.getHistorialMedico();
        
        this.historialMSelec.setPaciente(pacienteCita);
        
        this.detalleHistorialUpdate.setDiagnostico("CITA CANCELADA");
        this.detalleHistorialUpdate.setPeso("N/A");
        this.detalleHistorialUpdate.setPresion("N/A");
        this.citaSelect.setCompletado("NO_");
        
        actualizarHistorial();
        
    }

//    Una vez seleccionado la cita y dado en comenzar entonces se cargan los datos del paciente
    public void cargarHistorial() {
        Cita citaBuscar = new Cita();
        citaBuscar.setIdCita(Integer.parseInt(this.idCita));
        this.citaSelect = this.citaService.buscarCitaPorId(citaBuscar);

        Paciente pacienteCita = this.citaSelect.getIdUsuario();

        this.historialMSelec = pacienteCita.getHistorialMedico();
        //Agrego el paciente porque al recuperarlo en pacienteCita me devuelve vacio en historialMSelect
        this.historialMSelec.setPaciente(pacienteCita);

        this.detalleHistorial = this.historialMSelec.getDetalleHistorialList();

        this.idCita = "";

        PrimeFaces.current().executeInitScript("PF('dlgCita').hide();PF('dlgHistorial').show();");
    }

    public List<Cita> getCitasList() {
        return citasList;
    }

//    Metodo que ayuda a guardar los detalles de los hitoriales para guardarlos en la base de datos
    public void actualizarHistorial() {
//        Se deben de llenar datos importantes de detalle del historial
// ESTE METODO ES EL QUE HAY QUE VALIDAR PARA QUE EL USUARIO OBERVE SUS DIAGNOSTICOS YA ESTA RESUELTO
        Random rn = new Random();
        String codigo = "";
        for (int i = 0; i < 9; i++) {
            codigo += (char) (rn.nextInt(26) + 'A');
        }
        
        if(this.detalleHistorialUpdate.getPeso() == null) {
            this.detalleHistorialUpdate.setPeso("N/A");
            this.detalleHistorialUpdate.setPresion("N/A");
        }
        
        this.detalleHistorialUpdate.setCodigoDetalle(codigo);
        this.detalleHistorialUpdate.setFechaModificacion(new Date());
        this.detalleHistorialUpdate.setDiagnostico(this.detalleHistorialUpdate.getDiagnostico().toUpperCase());
        this.detalleHistorialUpdate.setPeso(this.detalleHistorialUpdate.getPeso().toUpperCase());
        this.detalleHistorialUpdate.setPresion(this.detalleHistorialUpdate.getPresion().toUpperCase());

        this.detalleHistorialUpdate.setIdUsuario(this.historialMSelec);
//        Como ya se proceso la cita entonces pasa a estado inactivo y se actualiza
        this.citaSelect.setActivo("INACTIVO");
        String cadenaC = this.citaSelect.getCompletado();
        this.citaSelect.setCompletado(cadenaC + "ATENDIDO");

        /*
        La solucion fue que despues de mandar a persistir el detalle de la factura como ya regresar con id entonces lo agrego a los elementos
        de la lista del historial medico en este momento, luego este historial lo mando a actualizarse y asi esta unidas todas las pertes
        para que pueda ser mostrado en el perfil del paciente.
         */
        try {
            List<DetalleHistorial> dh = this.historialMSelec.getDetalleHistorialList();
            dh.add(this.detalleHistorialUpdate);
            this.historialMSelec.setDetalleHistorialList(dh);

            this.historialService.actualizarHistorialMedico(this.historialMSelec);
            
            this.citaService.actualizarCita(this.citaSelect);

//            Se vuelve a cargar la lista de citas del empleado
            cargarCalendario();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualizado", "Se ha completado la cita"));
//            La unica forma de actualizar el caldenario es mediante este comando PF con myschedule
            PrimeFaces.current().executeScript("PF('dlgHistorial').hide();PF('myschedule').update()");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Se capturo un error"));
        } finally {
            this.detalleHistorialUpdate = new DetalleHistorial();
            this.historialMSelec = new HistorialMedico();
            PrimeFaces.current().ajax().update(":menuBar:msgs");
        }

    }

    public void onEventSelect(SelectEvent<ScheduleEvent<?>> selectEvent) {
        event = selectEvent.getObject();
        setIdCita(event.getDescription());
    }
}
