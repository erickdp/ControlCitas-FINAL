/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uce.proyect.web;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
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
import uce.proyect.domain.Consultorio;
import uce.proyect.domain.HorarioAtencion;
import uce.proyect.service.HorarioService;

/**
 *
 * @author Erick
 */
@Getter
@Setter
@ViewScoped
@Named
public class HorarioBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private HorarioService horarioService;

    private Date horaEntrada, horaSalida;

    private List<HorarioAtencion> horarioList;

    private HorarioAtencion horarioSelect;

    private String[] diasSeleccionados;

    private List<String> diasLaborales;

    private String cadenaDias;

    @PostConstruct
    public void init() {
        this.horarioList = this.horarioService.buscarTodosHorarios();
//        No agegar la funcion para fines de semana
        this.diasLaborales = new ArrayList<>();
        this.diasLaborales.add("LUNES");
        this.diasLaborales.add("MARTES");
        this.diasLaborales.add("MIERCOLES");
        this.diasLaborales.add("JUEVES");
        this.diasLaborales.add("VIERNES");
        this.diasLaborales.add("SABADO");
        this.diasLaborales.add("DOMINGO");
        this.horarioSelect = new HorarioAtencion();
    }

    public void cancelar() {
        this.horarioSelect = new HorarioAtencion();
    }

    //Metodo para agregar horarios
    public void agregarHorario() {

//        Primero se define si ha cambiado algun dia para el horario 
        this.cadenaDias = "";
        if (this.diasSeleccionados.length != 0) {
            for (int i = 0; i < this.diasSeleccionados.length; i++) {
                if (i == 0) {
                    this.cadenaDias += this.diasSeleccionados[i];
                } else {
                    this.cadenaDias += " " + this.diasSeleccionados[i];
                }
            }
            this.horarioSelect.setDiasLaborales(cadenaDias);
        }

        Date horaFormateadaI = restarMinutos(this.horarioSelect.getHoraInicio());
        Date horaFormateadaS = restarMinutos(this.horarioSelect.getHoraFinal());

        this.horarioSelect.setHoraInicio(horaFormateadaI);
        this.horarioSelect.setHoraFinal(horaFormateadaS);

        //Este if permite acceder al servicio el cual usa el siguiente Query HorarioAtencion.findByHoraInicioAndHoraFinal REVISAR el dao y service asociado
        //Si ya existe el horario puede que este actualizando los dias
        HorarioAtencion horarioBuscar = this.horarioService.buscarHorarioCompleto(this.horarioSelect);

        if (horarioBuscar == null) {

            //Si el horario no tiene codigo entonces ya existe y no es una insercion sino actualizacion
            if (this.horarioSelect.getCodigoHorario() == null) {

                Random rn = new Random();
                String codigo = "";
                for (int i = 0; i < 5; i++) {
                    codigo += (char) (rn.nextInt(26) + 'A');
                }
                this.horarioSelect.setCodigoHorario(codigo);
                this.horarioSelect.setEstado("ACTIVO");
                try {
                    this.horarioService.insertarHorarioAtencion(this.horarioSelect);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Agregado"));
                    PrimeFaces.current().ajax().update(":formHorario:dtHorario");
                    PrimeFaces.current().executeScript("PF('dlgGestionHorario').hide();");
                    this.diasSeleccionados = null;
                    this.horarioList = this.horarioService.buscarTodosHorarios();
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Se produjo un error"));
                    e.printStackTrace(System.out);
                } finally {
                    PrimeFaces.current().ajax().update("menuBar:msgs");
                    this.horarioSelect = new HorarioAtencion();
                }
            } else {
                actualizarHorario();
            }
//        Si esta actualizando los dias entonces no debe de haber uno igual en la base de datos
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "El horario ya existe, actualize sus campos"));
            PrimeFaces.current().ajax().update("menuBar:msgs");
        }

    }

    private Date restarMinutos(Date horaA) {
        
        Instant instan;
        LocalDateTime hora;

        instan = Instant.ofEpochMilli(horaA.getTime());
        hora = LocalDateTime.ofInstant(instan, ZoneId.systemDefault());

        hora = hora.minusMinutes(hora.getMinute());

        return java.sql.Timestamp.valueOf(hora);
    }

    public boolean validarCampo() {
        return this.horarioSelect.getCodigoHorario() == null;
    }

    public void eliminarHorario() {
        try {
            this.horarioService.eliminarHorarioAtencion(this.horarioSelect);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Horario Inhabilitado, tener"
                    + "en cuenta que los consultorios asociados pasan a estado INACTIVO"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Se produjo un error"));
            e.printStackTrace(System.out);
        } finally {
            PrimeFaces.current().ajax().update("menuBar:msgs");
            this.horarioSelect = new HorarioAtencion();
        }
    }

    public void habilitarHorario() {
        try {
            this.horarioSelect.setEstado("ACTIVO");
            List<Consultorio> listaConsultorios = this.horarioSelect.getConsultorioList();
            for (int i = 0; i < listaConsultorios.size(); i++) {
                listaConsultorios.get(i).setActivo("ACTIVO");
            }
            this.horarioSelect.setConsultorioList(listaConsultorios);
            this.horarioService.actualizarHorarioAtencion(this.horarioSelect);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Habilitado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Se produjo un error"));
            e.printStackTrace(System.out);
        } finally {
            PrimeFaces.current().ajax().update("menuBar:msgs");
            this.horarioSelect = new HorarioAtencion();
        }
    }

    public void actualizarHorario() {
        try {
            //Si solo cambio la hora no se actualizan los dias laborales, y viceversa
            if (!this.cadenaDias.equals("")) {
                this.horarioSelect.setDiasLaborales(this.cadenaDias);
            }
            this.horarioService.actualizarHorarioAtencion(this.horarioSelect);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Actualizado"));
            PrimeFaces.current().ajax().update(":formHorario:dtHorario");
            PrimeFaces.current().executeScript("PF('dlgGestionHorario').hide();");
            this.diasSeleccionados = null;
            this.horarioList = this.horarioService.buscarTodosHorarios();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Se produjo un error"));
            e.printStackTrace(System.out);
        } finally {
            PrimeFaces.current().ajax().update("menuBar:msgs");
            this.horarioSelect = new HorarioAtencion();
        }
    }

}
