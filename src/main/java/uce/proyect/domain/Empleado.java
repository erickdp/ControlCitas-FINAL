package uce.proyect.domain;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@NamedQueries({
    @NamedQuery(name = "Empleado.findAll", query = "SELECT e FROM Empleado e"),
    @NamedQuery(name = "Empleado.findByIdUsuario", query = "SELECT e FROM Empleado e WHERE e.idUsuario = :idUsuario"),
    @NamedQuery(name = "Empleado.findByCi", query = "SELECT e FROM Empleado e WHERE e.ci = :ci")

})
public class Empleado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_usuario")
    private int idUsuario;

    private String ci;

    private String nombre;

    private String apellido;

    private String cargo;

    private String activo;

//    Tener en vigilancia el actualizar en cascada
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", insertable = false, updatable = false)
    private Usuario usuario;

    @OneToOne(cascade = CascadeType.MERGE, mappedBy = "empleado")
    private Medico medico;

}
