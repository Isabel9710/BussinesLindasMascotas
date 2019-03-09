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
@Table(name = "cargos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cargos.findAll", query = "SELECT c FROM Cargos c")
    , @NamedQuery(name = "Cargos.findByIdCargo", query = "SELECT c FROM Cargos c WHERE c.idCargo = :idCargo")
    , @NamedQuery(name = "Cargos.findByNombreCargo", query = "SELECT c FROM Cargos c WHERE c.nombreCargo = :nombreCargo")})
public class Cargos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_cargo")
    private Integer idCargo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "nombre_cargo")
    private String nombreCargo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCargo")
    private List<InformacionLaboral> informacionLaboralList;

    public Cargos() {
    }

    public Cargos(Integer idCargo) {
        this.idCargo = idCargo;
    }

    public Cargos(Integer idCargo, String nombreCargo) {
        this.idCargo = idCargo;
        this.nombreCargo = nombreCargo;
    }

    public Integer getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(Integer idCargo) {
        this.idCargo = idCargo;
    }

    public String getNombreCargo() {
        return nombreCargo;
    }

    public void setNombreCargo(String nombreCargo) {
        this.nombreCargo = nombreCargo;
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
        hash += (idCargo != null ? idCargo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cargos)) {
            return false;
        }
        Cargos other = (Cargos) object;
        if ((this.idCargo == null && other.idCargo != null) || (this.idCargo != null && !this.idCargo.equals(other.idCargo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.bussineslm.entities.Cargos[ idCargo=" + idCargo + " ]";
    }
    
}
