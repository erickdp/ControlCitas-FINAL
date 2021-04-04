package uce.proyect.web.util;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import uce.proyect.domain.Empleado;

@Named
@RequestScoped
public class GestionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    //    Permite a quien gestione las citas cancelarlas, no es visto por el administrador o si esta inactiva la cita
    // Necesita un alcance de request es por esto que lo declaro en esta clase pues en otra con viewScope no sirve
    public boolean mostrarEliminar(Empleado empleadoGesion, String activo) {
        return !empleadoGesion.getCargo().equalsIgnoreCase("ADMINISTRADOR") && activo.equalsIgnoreCase("ACTIVO");
    }

    public boolean mostrarOpcion(String estado) {
        return estado.equalsIgnoreCase("ACTIVO");
    }

    public boolean mostrarOpcion2(String estado) {
        return estado.equalsIgnoreCase("INACTIVO");
    }

    public boolean validacionConsultorio(String estadoConsultorio, String estadoHorario) {
        return "INACTIVO".equals(estadoConsultorio) && "ACTIVO".equals(estadoHorario);
    }

    public boolean validacionConsultorio2(String estadoConsultorio, String estadoHorario) {
        return "ACTIVO".equals(estadoConsultorio) && "ACTIVO".equals(estadoHorario);
    }

    public String convertirHora(Date hora) {
//        Comversion de data a dateTime asi se arregla el error que aumenta 5 horas, es debido a que se usa Date, se debe de quitar el date converter del otro lado
        Instant instan = Instant.ofEpochMilli(hora.getTime());
        return LocalDateTime.ofInstant(instan, ZoneId.systemDefault()).toLocalTime().toString();
    }

    public String convertirFecha(Date hora) {
//        Comversion de data a dateTime asi se arregla el error que aumenta 5 horas, es debido a que se usa Date, se debe de quitar el date converter del otro lado
        Instant instan = Instant.ofEpochMilli(hora.getTime());
        LocalDateTime ldt = LocalDateTime.ofInstant(instan, ZoneId.systemDefault());
        return ldt.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm"));
    }

    public String convertirSoloFecha(Date fecha) {
        Instant instan = Instant.ofEpochMilli(fecha.getTime());
        LocalDateTime ldt = LocalDateTime.ofInstant(instan, ZoneId.systemDefault());
        return ldt.format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
    }

    public boolean cambiarSangre(String cadena) {
        return !cadena.equalsIgnoreCase("N/A");
    }

    public String validarActivo(String completado, String activo) {
        if (activo.equals("ACTIVO")) {
            return null;
        } else if (completado.equals("ATENDIDO")) {
            return "ATENDIDO";
        }
        return activo.equalsIgnoreCase("INACTIVO") && completado.equalsIgnoreCase("NO_ATENDIDO") ? "NO_ATENDIDO" : "INACTIVO";
    }

    public boolean validarEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

}
