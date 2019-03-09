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
@Table(name = "especies")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Especies.findAll", query = "SELECT e FROM Especies e")
    , @NamedQuery(name = "Especies.findByIdEspecie", query = "SELECT e FROM Especies e WHERE e.idEspecie = :idEspecie")
    , @NamedQuery(name = "Especies.findByNombreEspecie", query = "SELECT e FROM Especies e WHERE e.nombreEspecie = :nombreEspecie")})
public class Especies implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_especie")
    private Integer idEspecie;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombre_especie")
    private String nombreEspecie;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEspecie")
    private List<Razas> razasList;

    public Especies() {
    }

    public Especies(Integer idEspecie) {
        this.idEspecie = idEspecie;
    }

    public Especies(Integer idEspecie, String nombreEspecie) {
        this.idEspecie = idEspecie;
        this.nombreEspecie = nombreEspecie;
    }

    public Integer getIdEspecie() {
        return idEspecie;
    }

    public void setIdEspecie(Integer idEspecie) {
        this.idEspecie = idEspecie;
    }

    public String getNombreEspecie() {
        return nombreEspecie;
    }

    public void setNombreEspecie(String nombreEspecie) {
        this.nombreEspecie = nombreEspecie;
    }

    @XmlTransient
    @JsonIgnore
    public List<Razas> getRazasList() {
        return razasList;
    }

    public void setRazasList(List<Razas> razasList) {
        this.razasList = razasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEspecie != null ? idEspecie.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Especies)) {
            return false;
        }
        Especies other = (Especies) object;
        if ((this.idEspecie == null && other.idEspecie != null) || (this.idEspecie != null && !this.idEspecie.equals(other.idEspecie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.bussineslm.entities.Especies[ idEspecie=" + idEspecie + " ]";
    }
    
}
