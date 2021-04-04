package uce.proyect.web.util;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import uce.proyect.domain.Cita;
import uce.proyect.domain.Consultorio;
import uce.proyect.domain.DetalleHistorial;
import uce.proyect.domain.HistorialMedico;
import uce.proyect.domain.Paciente;
import uce.proyect.domain.Usuario;
import uce.proyect.service.CitaService;
import uce.proyect.service.ConsultorioService;
import uce.proyect.service.HistorialMedicoService;

// Clase que gestiona la tabla de citas tiene view scope y es capaz de mantener los datos durante la vista
@Getter
@Setter
@Named("conAdminBean")
@ViewScoped
public class CAdminBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private CitaService citaService;

    @Inject
    private ConsultorioService consultorioService;

    @Inject
    private HistorialMedicoService historialService;

    private List<Cita> citasList;

    private Cita citaSelect;

//    Agregar si hay tiempo el nombre en la barra de menu y cambiarlo por el nombre del admin
    @PostConstruct
    public void init() {

        this.citasList = citaService.buscarTodasCitas();
        this.citaSelect = new Cita();
        validarFechaCitas();

        Usuario usuario = (Usuario) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().get("usuario");

        if (!usuario.getEmpleado().getCargo().equalsIgnoreCase("ADMINISTRADOR")) {
            filtrarCitas();
        }

        Collections.sort(this.citasList);
    }

//    Este metodo se va a usar cuando quien se registre sea un empleado y solo necesite observar las citas activas
//    Pues el unico que necesita ver todas las citas es el administrador
    public void filtrarCitas() {
        for (int i = 0; i < this.citasList.size(); i++) {
            Cita citaActivo = this.citasList.get(i);
            if (citaActivo.getActivo().equalsIgnoreCase("INACTIVO") && (citaActivo.getCompletado().equalsIgnoreCase("")) || (citaActivo.getCompletado().equalsIgnoreCase("ATENDIDO"))) {
                this.citasList.remove(i);
                i--;
            }
        }
    }

    public boolean mostrarCitaActivo(String estado) {
        return estado.equalsIgnoreCase("ACTIVO");
    }

    public int getTotalCitas(Consultorio consultorioActual) {
        List<Cita> citas = this.consultorioService.buscarConsultorioPorEspecialidad(consultorioActual).getCitaList();
        int totalCitasActivas = 0;
        for (Cita citaActiva : citas) {
            if (citaActiva.getActivo().equalsIgnoreCase("ACTIVO")) {
                totalCitasActivas++;
            }
        }
        return totalCitasActivas++;
    }

//    Este metodo ayuda a actualizar automaticamente el estado de las fechas si no antes de la prevista
    public void validarFechaCitas() {
//        Convertir de date a tipo dateTime
        for (int i = 0; i < this.citasList.size(); i++) {
            Cita validarCita = this.citasList.get(i);
            if (validarCita.getActivo().equalsIgnoreCase("ACTIVO")) {

//            Pasar de date a localDateTime
                LocalDateTime fechaHora = new Timestamp(validarCita.getFechaCita().getTime()).toLocalDateTime();
                if (fechaHora.plusMinutes(59).isBefore(LocalDateTime.now())) {
                    validarCita.setActivo("INACTIVO");
                    validarCita.setCompletado("NO_ATENDIDO");
                }
            }
        }
//        Al acabar de validar todas las fechas entonces las actualizo en la base de datos
        this.citasList.forEach(citaValidada -> {
            this.citaService.actualizarCita(citaValidada);
        });
        this.citasList = this.citaService.buscarTodasCitas();
    }

    public void cargar(Cita citaEdit) {
        this.citaSelect = citaEdit;
    }

    public void cancelarCita() {

        Paciente paciente = this.citaSelect.getIdUsuario();
        HistorialMedico hm = paciente.getHistorialMedico();

        Random rn = new Random();
        String codigo = "";
        for (int i = 0; i < 9; i++) {
            codigo += (char) (rn.nextInt(26) + 'A');
        }

        DetalleHistorial dh = new DetalleHistorial();
        dh.setCodigoDetalle(codigo);
        dh.setPeso("N/A");
        dh.setPresion("N/A");
        dh.setFechaModificacion(new Date());
        dh.setDiagnostico("CITA CANCELADA");
        dh.setIdUsuario(hm);

        List<DetalleHistorial> dhList = hm.getDetalleHistorialList();
        dhList.add(dh);

        try {
            this.historialService.actualizarHistorialMedico(hm);
            this.citaService.eliminarCita(this.citaSelect);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
//        Se debe de eiminar de la lista a la cita cancelada para actualizar la tabla
//        this.citasList.remove(this.citaSelect);
    }

}
