package uce.proyect.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@NamedQueries({
    @NamedQuery(name = "Medico.findAll", query = "SELECT m FROM Medico m"),
    @NamedQuery(name = "Medico.findByIdUsuario", query = "SELECT m FROM Medico m WHERE m.idUsuario = :idUsuario")
})
public class Medico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_usuario")
    private int idUsuario;

    @ManyToOne
    @JoinColumn(name = "id_consultorio", referencedColumnName = "id_consultorio")
    private Consultorio idConsultorio;

    @OneToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", insertable = false, updatable = false)
    private Empleado empleado;

    @ManyToOne
    @JoinColumn(name = "id_horario_atencion", referencedColumnName = "id_horario_atencion")
    private HorarioAtencion idHorarioAtencion;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "idMedico")
    private List<Cita> citaList;
}
