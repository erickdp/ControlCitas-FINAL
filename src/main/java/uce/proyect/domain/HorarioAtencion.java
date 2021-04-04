package uce.proyect.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "horario_atencion")
@NamedQueries({
    @NamedQuery(name = "HorarioAtencion.findAll", query = "SELECT h FROM HorarioAtencion h"),
    @NamedQuery(name = "HorarioAtencion.findByIdHorarioAtencion", query = "SELECT h FROM HorarioAtencion h WHERE h.idHorarioAtencion = :idHorarioAtencion"),
    @NamedQuery(name = "HorarioAtencion.findByHoraInicioAndHoraFinal", query = "SELECT h FROM HorarioAtencion h WHERE h.horaInicio = :horaInicio AND h.horaFinal = :horaFinal"),
    @NamedQuery(name = "HorarioAtencion.findObject", query = "SELECT h FROM HorarioAtencion h WHERE h.diasLaborales = :diasLaborales AND h.horaInicio = :horaInicio AND h.horaFinal = :horaFinal")
})
public class HorarioAtencion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario_atencion")
    private int idHorarioAtencion;

    @Column(name = "codigo_horario")
    private String codigoHorario;

    @Column(name = "hora_inicio")
    @Temporal(TemporalType.TIME)
    private Date horaInicio;

    @Column(name = "hora_final")
    @Temporal(TemporalType.TIME)
    private Date horaFinal;

    @Column(name = "dias_laborales")
    private String diasLaborales;

    private String estado;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "idHorario")
    private List<Consultorio> consultorioList;

    @OneToMany(mappedBy = "idHorarioAtencion")
    private List<Medico> medicoList;

}
