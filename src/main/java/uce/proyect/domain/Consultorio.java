package uce.proyect.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@NamedQueries({
    
    @NamedQuery(name = "Consultorio.findAll", query = "SELECT c FROM Consultorio c"),
    @NamedQuery(name = "Consultorio.findByIdConsultorio", query = "SELECT c FROM Consultorio c WHERE c.idConsultorio = :idConsultorio"),
    @NamedQuery(name = "Consultorio.findByCodConsultorio", query = "SELECT c FROM Consultorio c WHERE c.codConsultorio = :codConsultorio"),
    @NamedQuery(name = "Consultorio.findByEspecialidad", query = "SELECT c FROM Consultorio c WHERE c.especialidad = :especialidad")
})
public class Consultorio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consultorio")
    private int idConsultorio;

    @Column(name = "cod_consultorio")
    private String codConsultorio;

    private String especialidad;

    private String activo;

    @ManyToOne
    @JoinColumn(name = "id_horario", referencedColumnName = "id_horario_atencion")
    private HorarioAtencion idHorario;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "idConsultorio")
    private List<Medico> medicoList;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE}, mappedBy = "idConsultorio")
    private List<Cita> citaList;

}
