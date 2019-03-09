/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bussineslm.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Isabel Medina
 */
@Entity
@Table(name = "tipo_servicios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoServicios.findAll", query = "SELECT t FROM TipoServicios t")
    , @NamedQuery(name = "TipoServicios.findByIdTipoServicio", query = "SELECT t FROM TipoServicios t WHERE t.idTipoServicio = :idTipoServicio")
    , @NamedQuery(name = "TipoServicios.findByNombreTipoServ", query = "SELECT t FROM TipoServicios t WHERE t.nombreTipoServ = :nombreTipoServ")})
public class TipoServicios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipo_servicio")
    private Integer idTipoServicio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombre_tipo_serv")
    private String nombreTipoServ;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoServicio")
    private List<Servicios> serviciosList;

    public TipoServicios() {
    }

    public TipoServicios(Integer idTipoServicio) {
        this.idTipoServicio = idTipoServicio;
    }

    public TipoServicios(Integer idTipoServicio, String nombreTipoServ) {
        this.idTipoServicio = idTipoServicio;
        this.nombreTipoServ = nombreTipoServ;
    }

    public Integer getIdTipoServicio() {
        return idTipoServicio;
    }

    public void setIdTipoServicio(Integer idTipoServicio) {
        this.idTipoServicio = idTipoServicio;
    }

    public String getNombreTipoServ() {
        return nombreTipoServ;
    }

    public void setNombreTipoServ(String nombreTipoServ) {
        this.nombreTipoServ = nombreTipoServ;
    }

    @XmlTransient
    @JsonIgnore
    public List<Servicios> getServiciosList() {
        return serviciosList;
    }

    public void setServiciosList(List<Servicios> serviciosList) {
        this.serviciosList = serviciosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoServicio != null ? idTipoServicio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoServicios)) {
            return false;
        }
        TipoServicios other = (TipoServicios) object;
        if ((this.idTipoServicio == null && other.idTipoServicio != null) || (this.idTipoServicio != null && !this.idTipoServicio.equals(other.idTipoServicio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.bussineslm.entities.TipoServicios[ idTipoServicio=" + idTipoServicio + " ]";
    }
    
}
