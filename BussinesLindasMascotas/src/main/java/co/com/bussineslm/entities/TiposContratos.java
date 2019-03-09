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
@Table(name = "tipos_contratos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TiposContratos.findAll", query = "SELECT t FROM TiposContratos t")
    , @NamedQuery(name = "TiposContratos.findByIdTipoContrato", query = "SELECT t FROM TiposContratos t WHERE t.idTipoContrato = :idTipoContrato")
    , @NamedQuery(name = "TiposContratos.findByNombreContrato", query = "SELECT t FROM TiposContratos t WHERE t.nombreContrato = :nombreContrato")})
public class TiposContratos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipo_contrato")
    private Integer idTipoContrato;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombre_contrato")
    private String nombreContrato;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoContrato")
    private List<InformacionLaboral> informacionLaboralList;

    public TiposContratos() {
    }

    public TiposContratos(Integer idTipoContrato) {
        this.idTipoContrato = idTipoContrato;
    }

    public TiposContratos(Integer idTipoContrato, String nombreContrato) {
        this.idTipoContrato = idTipoContrato;
        this.nombreContrato = nombreContrato;
    }

    public Integer getIdTipoContrato() {
        return idTipoContrato;
    }

    public void setIdTipoContrato(Integer idTipoContrato) {
        this.idTipoContrato = idTipoContrato;
    }

    public String getNombreContrato() {
        return nombreContrato;
    }

    public void setNombreContrato(String nombreContrato) {
        this.nombreContrato = nombreContrato;
    }

    @XmlTransient
    @JsonIgnore
    public List<InformacionLaboral> getInformacionLaboralList() {
        return informacionLaboralList;
    }

    public void setInformacionLaboralList(List<InformacionLaboral> informacionLaboralList) {
        this.informacionLaboralList = informacionLaboralList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoContrato != null ? idTipoContrato.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TiposContratos)) {
            return false;
        }
        TiposContratos other = (TiposContratos) object;
        if ((this.idTipoContrato == null && other.idTipoContrato != null) || (this.idTipoContrato != null && !this.idTipoContrato.equals(other.idTipoContrato))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.bussineslm.entities.TiposContratos[ idTipoContrato=" + idTipoContrato + " ]";
    }
    
}
