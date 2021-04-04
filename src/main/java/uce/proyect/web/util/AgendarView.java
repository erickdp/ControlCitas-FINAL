package uce.proyect.web.util;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Future;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import uce.proyect.domain.Cita;
import uce.proyect.domain.Consultorio;
import uce.proyect.domain.Empleado;
import uce.proyect.domain.Medico;
import uce.proyect.domain.Paciente;
import uce.proyect.domain.Usuario;
import uce.proyect.service.CitaService;
import uce.proyect.service.ConsultorioService;
import uce.proyect.service.EmpleadoService;
import uce.proyect.service.MedicoService;
import uce.proyect.service.PacienteService;
import uce.proyect.service.UsuarioService;


/*
Esta clase la utilizo para gestionar las consultas
que vaya a realizar el paciente
 */
@Getter
@Setter
@Named
@ViewScoped
public class AgendarView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private ConsultorioService consultorioService;

    @Inject
    private PacienteService pacienteService;

    @Inject
    private EmpleadoService empleadoService;

    @Inject
    private MedicoService medicoService;

    @Inject
    private CitaService citaService;

    @Inject
    private UsuarioService usuarioService;

    private String medicoSeleccionado;

    private Map<String, String> consultorios;

    private String consultorioSeleccionado;

    private Usuario usuarioSesion;

    private Usuario usuarioSelect;

    private Map<String, String> medicos;

    private List<Cita> citasList;

    private LocalDate fechaCitaADate;

    private Date fechaNacimiento;

    private LocalTime horaCitaADate;

    private Map<String, Integer> diasSemana;

    @Future
    private LocalTime horaMin;
    private LocalTime horaMax;
    private LocalDate minDate;
    private LocalDate fechaMin;
    private List<Integer> diasInvalidos;

    @PostConstruct
    public void init() {

//        Valido que el calendario este habilitado desde hoy y hasta un mes despues
        LocalDate ld = LocalDate.now();
        fechaMin = LocalDate.of(ld.getYear(), ld.getMonth(), ld.getDayOfMonth());

        cargarDias();

//        Obtengo la cita con la que se logeo el usuario
        usuarioSesion = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .get("usuario");
        this.usuarioSelect = new Usuario();
//        Esta variable sirve para cargar en la tabla las citas que haga, tener en cuenta que para actualizar
//                una tabla se debe de agregar ese elemento
        citasList = usuarioSesion.getPaciente().getCitaList();
//        Si no inicializo esta variable sale un error porque es usado en un menu
        this.diasInvalidos = new ArrayList<>();
        cargarConsultorios();
    }

    public void cargarDias() {
        diasSemana = new HashMap<>();

        diasSemana.put("LUNES", 1);
        diasSemana.put("MARTES", 2);
        diasSemana.put("MIERCOLES", 3);
        diasSemana.put("JUEVES", 4);
        diasSemana.put("VIERNES", 5);
        diasSemana.put("SABADO", 6);
        diasSemana.put("DOMINGO", 0);

    }

    public void actualizarUsuario() {

        try {
            Paciente pacienteUpper = this.usuarioSelect.getPaciente();
            pacienteUpper.setPGenero(pacienteUpper.getPGenero().toUpperCase());
            pacienteUpper.setPApellido(pacienteUpper.getPApellido().toUpperCase());
            pacienteUpper.setPNombre(pacienteUpper.getPNombre().toUpperCase());
            this.usuarioSelect.setPaciente(pacienteUpper);
            this.usuarioSelect.setNombreUsuario(this.usuarioSelect.getNombreUsuario().trim());
            this.usuarioService.actualizarUsuario(this.usuarioSelect);
            this.usuarioSesion = this.usuarioService.buscarUsuarioPorName(this.usuarioSelect.getNombreUsuario());
            this.usuarioSelect = new Usuario();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Aviso", "Se ha actualizado correctamente el empleado"));
            PrimeFaces.current().ajax().update(":formPerfil:panelGrid");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL,
                            "Aviso", "No se ha actualizado correctamente el empleado"));
        } finally {
            PrimeFaces.current().executeScript("PF('dlgUsuario').hide();");
            PrimeFaces.current().ajax().update(":menuBar:msgs");
        }
    }

    /*
    Metodo que va a usar Ajax donde dependiendo de la especialidad se cargan los medicos
     */
    public void cargarConsultorios() {
        consultorios = new HashMap<>();
        for (Consultorio consultorio : consultorioService.buscarTodosConsultorios()) {
            if (consultorio.getActivo().equalsIgnoreCase("ACTIVO")) {
                consultorios.put(consultorio.getEspecialidad(), consultorio.getEspecialidad());
            }
        }
    }

    public void cargarMedicos() {
//        Este es un mensaje para asegurar de que ajaxe esta cargando, se usa un Listener
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Alerta", "Se actualizaron los campos siguietes"));
        medicos = new HashMap<>();
        if (this.consultorioSeleccionado != null) {

            Consultorio con = new Consultorio();
            con.setEspecialidad(this.consultorioSeleccionado);

//            Cargo los horarios de los consultorios para no pasarme del horario que ellos atienden
            con = consultorioService.buscarConsultorioPorEspecialidad(con);

            DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");

            controlarCalendario(con.getIdHorario().getDiasLaborales());

            horaMin = LocalTime.parse(formatoHora.format(con.getIdHorario().getHoraInicio()));
            horaMax = LocalTime.parse(formatoHora.format(con.getIdHorario().getHoraFinal()));

            for (Medico medico : con
                    .getMedicoList()) {

//                En el menu desplegable se muestra la llave solo acepta String y por eso guardo el valor como la cedula
//                Si mando otro dato empieza a dar un error
                medicos.put("Dr." + medico.getEmpleado().getNombre()
                        + " " + medico.getEmpleado().getApellido(), medico.getEmpleado().getCi());
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
                    "Alerta", "No se han cargado los medicos"));
        }
    }

//    Esta funcionalidad me permite determinar solo los dias que debo deshabilitar
//    Por ejemplo si trabaja de L-V debo quitar sabado y domingo
    public void controlarCalendario(String diasLaborales) {
        cargarDias();
        this.diasInvalidos = new ArrayList<>();
        String[] dias = diasLaborales.split(" ");
        for (String dia : dias) {
            this.diasSemana.remove(dia);
        }

//        Los dias que me sobren seran los que deba deshabilitar porque son los dias que no atienden
        for (Map.Entry<String, Integer> entry : this.diasSemana.entrySet()) {
            Integer value = entry.getValue();
            this.diasInvalidos.add(value);
        }

    }

    public void agregarCita() {

        //            Lo transformo a tipo date porque localdate no maneja la base de datos
//            Impedir que el usuario maneje los minutos y que solo se guarden las citas por horas, validar eso, un ejemplo
        int minutosAgregados = horaCitaADate.getMinute();
        if (minutosAgregados != 0) {
            this.horaCitaADate = this.horaCitaADate.minusMinutes(minutosAgregados);
        }

        //        Le resto 5 horas al tiempo que haya puesto para que se guarde correctamenete, si les falla esto entonces borrarlo
        try {
            LocalDateTime ldtADate = LocalDateTime.of(fechaCitaADate, horaCitaADate);
            if(ldtADate.isBefore(LocalDateTime.now())) {
                throw new Exception("La fecha ingresada es antigua, intente con otro horario");
            }
            //INSTANCIAMOS UNA ZONA HORARIA

            ZoneId zoneId = ZoneId.of("America/Guayaquil");
            //CREAMOS UNA FECHA CON ZONA HORARIA ARREGLADA
            ZonedDateTime zdt = ZonedDateTime.of(ldtADate, zoneId);

            Date horario = Date.from(zdt.toInstant());

            Cita citaBuscar = new Cita();
            citaBuscar.setIdUsuario(this.usuarioSesion.getPaciente());
            citaBuscar.setFechaCita(horario);

            //Como su nombre lo dice, si esta tratando de agregar otra cita a la misma hora, misma fecha y mismo usuario no es posible
            if (this.citaService.buscarCitasConcurrentes(citaBuscar) != null) {
                throw new Exception("Está tratando de agregar una cita concurrente.");
            }

            Cita nuevaCita = new Cita();
//        En la base de datos esta validado para que la fecha de la cita sea unica
//          Tratar de resolver que solo pueda escojer entre horas las citas
//            Para agreagar una cita se necesita, el medico, paciente, y consultorio como objetos y almacenarlos
            Consultorio consultorioN = new Consultorio();
            consultorioN.setEspecialidad(this.consultorioSeleccionado);
            consultorioN = this.consultorioService.buscarConsultorioPorEspecialidad(consultorioN);

            Empleado empleadoN = new Empleado();
            empleadoN.setCi(this.medicoSeleccionado);
            Medico medicoN = empleadoService.buscarEmpleadoPorCi(empleadoN).getMedico(); //No me devuelve el medico aqui

            Paciente pacienteCita = this.usuarioSesion.getPaciente();

            List<Cita> citasP, citasM, citasC;
            citasM = medicoN.getCitaList();
            validarFechaCita(ldtADate, citasM);

            nuevaCita.setIdConsultorio(consultorioN);

            nuevaCita.setIdMedico(medicoN);

            nuevaCita.setIdUsuario(pacienteCita);

            nuevaCita.setFechaCita(horario);

            Random rn = new Random();
            String codigo = "";
            for (int i = 0; i < 8; i++) {
                codigo += (char) (rn.nextInt(26) + 'A');
            }

            nuevaCita.setCodigoCita(codigo);
            nuevaCita.setActivo("ACTIVO");
            nuevaCita.setCompletado("");

            //MUY IMPORTANTE USO DE CASCADA PARA PERSISTIR O SEA GUARDAR Y MERGE PARA ACTUALIZAR
            //Surgio un bug en donde al momento de persistir la cita con su respectivo EJB este se caia al alcualizalo para el consultorio
            //Por esto se agrego persistencia en cascada para consultorio en el mapeo de citas, si la cita no tiene id lo agrega
            citasC = consultorioN.getCitaList();
            citasC.add(nuevaCita);
            this.consultorioService.actualizarConsultorio(consultorioN);

            //COMO YA TENGO UN ID EN LA CITA LE PIDO PARA AGREGARLO EN LAS CITAS DEL PACINETE y medico
            Cita citaKey = new Cita();
            citaKey.setCodigoCita(codigo);
            citaKey = this.citaService.buscarCitaPorCodigo(citaKey);

            citasM.add(citaKey);
            this.medicoService.actualizarMedico(medicoN);

            citasP = pacienteCita.getCitaList();
            citasP.add(citaKey);

            //Este tiene merge para las citas asi que no le da un nuevo id
//            Actualizo las citas en el consultorio para que se sincronicen
            this.pacienteService.actuaizarPaciente(pacienteCita);

//            Usado para mandar mensajes de respuesta, se debe de tener un p:dialog
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Alerta", "Se ha agregado una nueva cita"));
//            Hago que una vez terminado el proceso se cierre el dialog
        } catch (Exception e) {
            e.printStackTrace(System.out);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
                    "Alerta", e.getMessage()));
        } finally {
            PrimeFaces.current().executeScript("PF('dlgCita').hide()");
//            Para que se muestren los mensajes se debe de actualizar el grow 
            PrimeFaces.current().ajax().update(":menuBar:msgs", ":formCitas:dtCitas");
            this.consultorioSeleccionado = null;
            this.medicoSeleccionado = null;
            this.fechaCitaADate = null;
            this.horaCitaADate = null;
            this.horaMax = null;
            this.horaMin = null;
        }
    }

    public void validarFechaCita(LocalDateTime fechaCita, List<Cita> citas) throws Exception {
        for (Cita citaOcupada : citas) {
//            Metodo para pasar de date a localdateTime
            LocalDateTime fechaCitaAnterior = citaOcupada.getFechaCita().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            if (citaOcupada.getActivo().equalsIgnoreCase("ACTIVO") && fechaCita.isEqual(fechaCitaAnterior)) {
                throw new Exception("No se puede seleccionar este horario, pues no está disponible");
            }
        }
    }
}
