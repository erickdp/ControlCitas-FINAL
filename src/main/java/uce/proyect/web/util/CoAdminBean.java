package uce.proyect.web.util;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
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
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.primefaces.event.RowEditEvent;
import uce.proyect.domain.Cita;
import uce.proyect.domain.Consultorio;
import uce.proyect.domain.Empleado;
import uce.proyect.domain.HorarioAtencion;
import uce.proyect.domain.Medico;
import uce.proyect.service.ConsultorioService;
import uce.proyect.service.EmpleadoService;
import uce.proyect.service.HorarioService;

@Getter
@Setter
@Named("consultorioAdminBean")
@ViewScoped
public class CoAdminBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private ConsultorioService consultorioService;

    @Inject
    private HorarioService horarioService;

    @Inject
    private EmpleadoService empleadoService;

    private Consultorio consultorioSelect;

    private List<Consultorio> consultorioList;

    private String horaApertura;

    private String especialidad;

    private String medicoId;

    private Map<String, String> horarios;

//    Declaro como string el mapa y no como objeto y llave porque para los menus con opciones solo puede manejar string
    private Map<String, String> consultorioMapList;

    private Map<String, String> medicosMapList;

    //Bandera usada para visualizar el boton de agregar o deshabilitar consultorio
    private boolean bandera;

    private List<Empleado> empleadoList;

    @PostConstruct
    public void init() {
        this.consultorioSelect = new Consultorio();
        this.consultorioList = this.consultorioService.buscarTodosConsultorios();
        this.empleadoList = new ArrayList<>();
        cargarHorarios();
    }

//    Si uso p:dataExporter y un listener junto no manda los componentes a cargarse lo manda null por eso primero lo cargo y en un dialogo lo mando a exportar
    public void cargarReporte(String consultorio) {
//        Este metodo de service utiliza una consulta PLSQL mirar y practicar
        this.empleadoList = this.consultorioService.obtenerReporte(consultorio);
    }

//    Consultar como cargar este metodo sin tener que hacerlo en el post construct pista p:outputpanel
    public void cargarCamposAgregar() {
//        Este carga los consultorios activos
        this.consultorioMapList = new HashMap<>();
        for (Consultorio consultorioValido : this.consultorioList) {
            if (consultorioValido.getActivo().equalsIgnoreCase("ACTIVO")) {
                this.consultorioMapList.put(consultorioValido.getEspecialidad(), String.valueOf(consultorioValido.getIdConsultorio()));
            }
        }

//        Este carga a los medicos que no tienen consutorio
        this.medicosMapList = new HashMap<>();
        for (Empleado empeladoMedico : this.empleadoService.buscarTodosEmpleados()) {
            if ((empeladoMedico.getCargo().equalsIgnoreCase("MEDICO") & empeladoMedico.getActivo().equalsIgnoreCase("ACTIVO"))
                    && empeladoMedico.getMedico() == null) {
                this.medicosMapList.put(empeladoMedico.getNombre() + " " + empeladoMedico.getApellido(), String.valueOf(empeladoMedico.getIdUsuario()));
            }
        }

    }

    public void cargarHorarios() {

        horarios = new HashMap<>();
        List<HorarioAtencion> ha = this.horarioService.buscarTodosHorarios();
        Instant instan;
        LocalTime horaInicio, horaFinal;

        for (HorarioAtencion horario : ha) {
            if (horario.getEstado().equalsIgnoreCase("INACTIVO")) {
                continue;
            }
//            De date a localTime
            instan = Instant.ofEpochMilli(horario.getHoraInicio().getTime());
            horaInicio = LocalDateTime.ofInstant(instan, ZoneId.systemDefault()).toLocalTime();

            instan = Instant.ofEpochMilli(horario.getHoraFinal().getTime());
            horaFinal = LocalDateTime.ofInstant(instan, ZoneId.systemDefault()).toLocalTime();

            horarios.put(horaInicio + " A " + horaFinal + " " + horario.getDiasLaborales(),
                    String.valueOf(horario.getIdHorarioAtencion()));

        }
    }

    public Consultorio getConsultorioSelect() {
        return consultorioSelect;
    }

    //Si cambia el estado del consultorio entonces tambien todas sus citas lo haran, excepto los medicos de dicho cnsultorio
    public void inhabilitarConsultorio() {
        List<Cita> citasInhabilitar = this.consultorioSelect.getCitaList();
        int contadorCitas = 0;
        for (int i = 0; i < citasInhabilitar.size(); i++) {
            if (citasInhabilitar.get(i).getCompletado().equalsIgnoreCase("")) {
                citasInhabilitar.get(i).setActivo("INACTIVO");
                contadorCitas++;
            }
        }
        this.consultorioSelect.setCitaList(citasInhabilitar);
        try {
            this.consultorioService.eliminarConsultorio(this.consultorioSelect);
            this.consultorioSelect = new Consultorio();
//            Se deben de actualizar los consultorios a los que pueden ser agregados los medicos
            cargarCamposAgregar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Habilitado", "Se han inhabilitado " + contadorCitas + " citas."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "Se ha generado un error"));
        } finally {
            PrimeFaces.current().ajax().update(":menuBar:msgs");
        }
    }

    public int countEspecialistas(List<Medico> medicosList) {
        return medicosList.size();
    }

    //Si cambio el estado del consultorio entonces sus citas vuelven a estar activas, pero solo las que su fecha es despues de la actual
    public void habilitarConsultorio() {
        List<Cita> citasInhabilitar = this.consultorioSelect.getCitaList();
        Cita validarCita;
        int contadorCitas = 0;
        for (int i = 0; i < citasInhabilitar.size(); i++) {
            validarCita = citasInhabilitar.get(i);
            LocalDateTime validarHora = new Timestamp(validarCita.getFechaCita().getTime()).toLocalDateTime();
//            Si la cita ya fue atendida o cancelada entonces es mejor ya no validar la hora
            if (validarCita.getCompletado().equalsIgnoreCase("") && validarHora.isAfter(LocalDateTime.now())) {
                validarCita.setActivo("ACTIVO");
                contadorCitas++;
            }
        }
        this.consultorioSelect.setActivo("ACTIVO");
        try {
            this.consultorioService.actualizarConsultorio(this.consultorioSelect);
            //            Se deben de actualizar los consultorios a los que pueden ser agregados los medicos
            cargarCamposAgregar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Habilitado", "Se han habilitado " + contadorCitas + " citas."));
//            Linea agregada
            this.consultorioSelect = new Consultorio();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "Se ha generado un error"));
        } finally {
            PrimeFaces.current().ajax().update(":menuBar:msgs");
        }
    }

    public void onRowEdit(RowEditEvent<Consultorio> evt) {

        try {
            Consultorio consultorioEdit = evt.getObject();
            consultorioEdit.setEspecialidad(consultorioEdit.getEspecialidad().toUpperCase());
            consultorioEdit.setCodConsultorio(consultorioEdit.getCodConsultorio().toUpperCase());

            this.consultorioService.actualizarConsultorio(consultorioEdit);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualizado", "Consultorio Actualizado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "A surgido un error"));
        }

    }

    //Metodo para agreagar un medico a un consultorio
    public void agregarMedicoAConsultorio() {

        try {

            //Como el mapa me devuelve el valor yo guarde ese id y lo tomo
            Empleado empleadoSelect = new Empleado();
            empleadoSelect.setIdUsuario(Integer.parseInt(this.medicoId));
            empleadoSelect = this.empleadoService.buscarEmpleadoPorId(empleadoSelect);

//            Para actualizar ambas partes debo tener en cuenta que debo de agregarle el objeto para que se actualice
            this.consultorioSelect = new Consultorio();
            this.consultorioSelect.setIdConsultorio(Integer.parseInt(this.especialidad));
            this.consultorioSelect = this.consultorioService.buscarConsultorioPorId(this.consultorioSelect);

            Medico medicoNuevo = new Medico();
            medicoNuevo.setIdUsuario(empleadoSelect.getIdUsuario());
            medicoNuevo.setCitaList(new ArrayList<>());
            medicoNuevo.setIdHorarioAtencion(this.consultorioSelect.getIdHorario());

            medicoNuevo.setIdConsultorio(this.consultorioSelect);
            empleadoSelect.setMedico(medicoNuevo);
            medicoNuevo.setEmpleado(empleadoSelect);

            List<Medico> list = this.consultorioSelect.getMedicoList();
            list.add(medicoNuevo);
            this.consultorioSelect.setMedicoList(list);
//            Al actualizar el consultorio como ya tiene una referencia hacia el medico entonces se guardaran con sus fk
//            this.empleadoService.updateEmpleado(empleadoSelect);
            this.consultorioService.actualizarConsultorio(this.consultorioSelect);
//            En algunos casos se deben de actualizar ambos lados
            this.empleadoService.actualizarEmpleado(empleadoSelect);

            this.consultorioList = this.consultorioService.buscarTodosConsultorios();

//            Restauro nuevamente los valores y ejecuto la comprobacion para que no se repitan los medicos en la lista
            this.medicoId = "";
            this.especialidad = "";
            cargarCamposAgregar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Agregado", "Se ha agregado correctamente"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "Se ha generado un error"));
        } finally {
            PrimeFaces.current().ajax().update(":menuBar:msgs");
            this.horaApertura = "";
        }

    }

    public void cancelarOpcion() {
        this.consultorioSelect = new Consultorio();
    }

    public void agregarConsultorio() {

        this.consultorioSelect.setEspecialidad(this.consultorioSelect.getEspecialidad().toUpperCase());
        Consultorio consultorioBuscar = this.consultorioService.
                buscarConsultorioPorCodigo(this.consultorioSelect);
//        Si no tiene codigo es decir que es un nuevo consultorio, si intenta repetir la especialidad igual la base de datos no permitira eso
        if (consultorioBuscar == null) {

            this.consultorioSelect.setActivo("ACTIVO");
            this.consultorioSelect.setCitaList(new ArrayList<>());

            Random rn = new Random();
            String codigo = "";
            for (int i = 0; i < 9; i++) {
                codigo += (char) (rn.nextInt(26) + 'A');
            }

            this.consultorioSelect.setCodConsultorio(codigo);
            HorarioAtencion ha = new HorarioAtencion();
            ha.setIdHorarioAtencion(Integer.parseInt(this.horaApertura));

            ha = this.horarioService.buscarHorarioPorId(ha);
            this.consultorioSelect.setIdHorario(ha);
            try {
                this.consultorioService.guardarConsultorio(this.consultorioSelect);
//                No es necesario obtener toda la lista asociada, solo se actualiza para guardar la referencia
                this.horarioService.actualizarHorarioAtencion(ha);
                this.consultorioList = this.consultorioService.buscarTodosConsultorios();
                cargarCamposAgregar();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Agregado", "Se ha agregado correctamente"));
                PrimeFaces.current().ajax().update(":formConsultorio:dtConsultorio");
                PrimeFaces.current().executeScript("PF('dlgReporteAgregarConsultorio').hide();");
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Error", "Se ha generado un error"));
            } finally {
                this.consultorioSelect = new Consultorio();
                this.horaApertura = "";
            }
        } else {
//            Si el selectonemenu no a sido topado entonces no esta actualizando su horario
            if (this.horaApertura != null) {
                HorarioAtencion ha = new HorarioAtencion();
                ha.setIdHorarioAtencion(Integer.parseInt(this.horaApertura));

//                Debo eliminar del horario que tenia antes el consultorio y asi no siga guardando la referencia
                HorarioAtencion horarioAntiguo = this.horarioService.buscarHorarioPorId(this.consultorioSelect.getIdHorario());
                List<Consultorio> listaAntigua = horarioAntiguo.getConsultorioList();
                for (int i = 0; i < listaAntigua.size(); i++) {
                    if (listaAntigua.get(i).getEspecialidad().equalsIgnoreCase(this.consultorioSelect.getEspecialidad())) {
                        listaAntigua.remove(i);
                        break;
                    }
                }

                this.horarioService.actualizarHorarioAtencion(horarioAntiguo);

                ha = this.horarioService.buscarHorarioPorId(ha);
                this.consultorioSelect.setIdHorario(ha);
                List<Consultorio> list = ha.getConsultorioList();
                list.add(this.consultorioSelect);

                this.horarioService.actualizarHorarioAtencion(ha);
            }

            try {
                this.consultorioService.actualizarConsultorio(this.consultorioSelect);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Actualizado", "Se ha actualizado correctamente"));
                PrimeFaces.current().ajax().update(":formConsultorio:dtConsultorio");
                PrimeFaces.current().executeScript("PF('dlgReporteAgregarConsultorio').hide();");
                cargarCamposAgregar();
                this.consultorioList = this.consultorioService.buscarTodosConsultorios();

            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Se ha producido un Error"));

            } finally {
//                Tratar siempre con un try catch porque aveces se pone raro
                this.consultorioSelect = new Consultorio();
                this.horaApertura = "";
            }
        }
    }
}
