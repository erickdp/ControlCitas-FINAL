package uce.proyect.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@NamedQueries({
    
    @NamedQuery(name = "Cita.findAll", query = "SELECT c FROM Cita c"),
    @NamedQuery(name = "Cita.findByIdCita", query = "SELECT c FROM Cita c WHERE c.idCita = :idCita"),
    @NamedQuery(name = "Cita.findByCodigoCita", query = "SELECT c FROM Cita c WHERE c.codigoCita = :codigoCita"),
    @NamedQuery(name = "Cita.findByfechaCitaAndPaciente", query = "SELECT c FROM Cita c WHERE c.fechaCita = :fechaCita AND c.idUsuario = :idPaciente"),
    @NamedQuery(name = "Cita.test", query = "SELECT c.activo, c.codigoCita FROM Cita c WHERE c.fechaCita = :fechaCita AND c.idUsuario = :idPaciente")
})
public class Cita implements Serializable, Comparable<Cita> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private int idCita;

    @Column(name = "codigo_cita")
    private String codigoCita;

    @Column(name = "fecha_cita")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCita;

    private String activo;

//    Nuevo atributo agregago que ayuda a la validacion de citas completadas y no completadas, independizando al estado de la cita
    private String completado;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "id_consultorio", referencedColumnName = "id_consultorio")
    private Consultorio idConsultorio;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "id_medico", referencedColumnName = "id_usuario")
    private Medico idMedico;
    //Antes no estaba cascada borrar porque es un testeo, vale con refresh voy a probar merge
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Paciente idUsuario;

    @Override
    public int compareTo(Cita o) {
        if (this.idConsultorio.getIdConsultorio() > o.getIdConsultorio().getIdConsultorio()) {
            return 1;
        } else if (this.idConsultorio.getIdConsultorio() < o.getIdConsultorio().getIdConsultorio()) {
            return -1;
        }
        return 0;
    }

}
