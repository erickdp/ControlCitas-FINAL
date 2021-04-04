package uce.proyect.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@NamedQueries({
    @NamedQuery(name = "HistorialMedico.findAll", query = "SELECT h FROM HistorialMedico h"),
    @NamedQuery(name = "HistorialMedico.findByIdUsuario", query = "SELECT h FROM HistorialMedico h WHERE h.idUsuario = :idUsuario"),
    @NamedQuery(name = "HistorialMedico.findByCodigoHistorial", query = "SELECT h FROM HistorialMedico h WHERE h.codigoHistorial = :codigoHistorial")

})
@Table(name = "historial_medico")
public class HistorialMedico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_usuario")
    private int idUsuario;

    @Column(name = "codigo_historial")
    private String codigoHistorial;

    @Column(name = "tipo_sangre")
    private String tipoSangre;

    @Column(name = "fecha_apertura")
    @Temporal(TemporalType.DATE)
    private Date fechaApertura;

    @OneToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", insertable = false, updatable = false)
    private Paciente paciente;
    //Cuando pongo persistir significa que va a crear una PK para el registro que no tenga, o sea el nuevo detalle se le asigna una PK
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "idUsuario")
    private List<DetalleHistorial> detalleHistorialList;

}
