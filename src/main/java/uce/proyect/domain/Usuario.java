package uce.proyect.domain;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findByIdUsuario", query = "SELECT u FROM Usuario u WHERE u.idUsuario = :idUsuario"),
    @NamedQuery(name = "Usuario.findUsuarioByName", query = "SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario"),
    @NamedQuery(name = "Usuario.findByNombreUsuarioAndContrasena", query = "SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario AND u.contrasena = :contrasena")
})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private int idUsuario;

    @Column(name = "nombre_usuario")
    private String nombreUsuario;

    private String contrasena;

    private String telefono;

    private String mail;

//    Si no hay cascada en este lado el empleado no se actualiza, primero es merge y luego refresh
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "usuario")
    private Empleado empleado;

//    Lo mismo se arriba para el paciente ahora
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "usuario")
    private Paciente paciente;
}
