/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bussineslm.entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Isabel Medina
 */
@Entity
@Table(name = "servicios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Servicios.findAll", query = "SELECT s FROM Servicios s")
    , @NamedQuery(name = "Servicios.findByIdServicio", query = "SELECT s FROM Servicios s WHERE s.idServicio = :idServicio")
    , @NamedQuery(name = "Servicios.findByNombreServicio", query = "SELECT s FROM Servicios s WHERE s.nombreServicio = :nombreServicio")
    , @NamedQuery(name = "Servicios.findByPrecioServicio", query = "SELECT s FROM Servicios s WHERE s.precioServicio = :precioServicio")
    , @NamedQuery(name = "Servicios.findByDescripcionServicio", query = "SELECT s FROM Servicios s WHERE s.descripcionServicio = :descripcionServicio")})
public class Servicios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_servicio")
    private Integer idServicio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombre_servicio")
    private String nombreServicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_servicio")
    private BigInteger precioServicio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "descripcion_servicio")
    private String descripcionServicio;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "idServicio")
    private ServiciosPorEmpleados serviciosPorEmpleados;
    @JoinColumn(name = "id_tipo_servicio", referencedColumnName = "id_tipo_servicio")
    @ManyToOne(optional = false)
    private TipoServicios idTipoServicio;

    public Servicios() {
    }

    public Servicios(Integer idServicio) {
        this.idServicio = idServicio;
    }

    public Servicios(Integer idServicio, String nombreServicio, BigInteger precioServicio, String descripcionServicio) {
        this.idServicio = idServicio;
        this.nombreServicio = nombreServicio;
        this.precioServicio = precioServicio;
        this.descripcionServicio = descripcionServicio;
    }

    public Integer getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Integer idServicio) {
        this.idServicio = idServicio;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public BigInteger getPrecioServicio() {
        return precioServicio;
    }

    public void setPrecioServicio(BigInteger precioServicio) {
        this.precioServicio = precioServicio;
    }

    public String getDescripcionServicio() {
        return descripcionServicio;
    }

    public void setDescripcionServicio(String descripcionServicio) {
        this.descripcionServicio = descripcionServicio;
    }

    public ServiciosPorEmpleados getServiciosPorEmpleados() {
        return serviciosPorEmpleados;
    }

    public void setServiciosPorEmpleados(ServiciosPorEmpleados serviciosPorEmpleados) {
        this.serviciosPorEmpleados = serviciosPorEmpleados;
    }

    public TipoServicios getIdTipoServicio() {
        return idTipoServicio;
    }

    public void setIdTipoServicio(TipoServicios idTipoServicio) {
        this.idTipoServicio = idTipoServicio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idServicio != null ? idServicio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Servicios)) {
            return false;
        }
        Servicios other = (Servicios) object;
        if ((this.idServicio == null && other.idServicio != null) || (this.idServicio != null && !this.idServicio.equals(other.idServicio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.bussineslm.entities.Servicios[ idServicio=" + idServicio + " ]";
    }
    
}
