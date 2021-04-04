package uce.proyect.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@NamedQueries({
    @NamedQuery(name = "DetalleHistorial.findAll", query = "SELECT d FROM DetalleHistorial d"),
    @NamedQuery(name = "DetalleHistorial.findByIdDetalleHistorial", query = "SELECT d FROM DetalleHistorial d WHERE d.idDetalleHistorial = :idDetalleHistorial"),
    @NamedQuery(name = "DetalleHistorial.findByCodigoDetalle", query = "SELECT d FROM DetalleHistorial d WHERE d.codigoDetalle = :codigoDetalle")
})
@Table(name = "detalle_historial")
public class DetalleHistorial implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_historial")
    private int idDetalleHistorial;

    @Column(name = "codigo_detalle")
    private String codigoDetalle;

    private String peso;

    private String presion;

    private String diagnostico;

    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;

    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private HistorialMedico idUsuario;

}
