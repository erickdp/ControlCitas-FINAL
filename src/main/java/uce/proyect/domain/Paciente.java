package uce.proyect.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@NamedQueries({
    @NamedQuery(name = "Paciente.findAll", query = "SELECT p FROM Paciente p"),
    @NamedQuery(name = "Paciente.findByIdUsuario", query = "SELECT p FROM Paciente p WHERE p.idUsuario = :idUsuario"),
    @NamedQuery(name = "Paciente.findByPCi", query = "SELECT p FROM Paciente p WHERE p.pCi = :pCi")
})
public class Paciente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_usuario")
    private int idUsuario;

    @Column(name = "p_ci")
    private String pCi;

    @Column(name = "p_nombre")
    private String pNombre;

    @Column(name = "p_apellido")
    private String pApellido;

    @Column(name = "p_fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date pFechaNacimiento;

    @Column(name = "p_genero")
    private String pGenero;

    private String activo;

    @OneToOne(mappedBy = "paciente")
    private HistorialMedico historialMedico;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", insertable = false, updatable = false)
    private Usuario usuario;
//    A la nueva cita que se le agrege le doy un PK pero tengo que obtener todas las listas agregarlas y actualizar al paciente
    @OneToMany(mappedBy = "idUsuario")
    private List<Cita> citaList;

}
