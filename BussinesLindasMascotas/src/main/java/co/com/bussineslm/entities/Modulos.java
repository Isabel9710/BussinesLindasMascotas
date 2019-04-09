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
@Table(name = "modulos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Modulos.findAll", query = "SELECT m FROM Modulos m")
    , @NamedQuery(name = "Modulos.findByIdModulo", query = "SELECT m FROM Modulos m WHERE m.idModulo = :idModulo")
    , @NamedQuery(name = "Modulos.findByNombreModulo", query = "SELECT m FROM Modulos m WHERE m.nombreModulo = :nombreModulo")})
public class Modulos implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "nombreCtrl")
    private String nombreCtrl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "icono")
    private String icono;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_modulo")
    private Integer idModulo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombre_modulo")
    private String nombreModulo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idModulo")
    private List<SubModulos> subModulosList;

    public Modulos() {
    }

    public Modulos(Integer idModulo) {
        this.idModulo = idModulo;
    }

    public Modulos(String nombreModulo, String nombreCtrl, String icono, List<SubModulos> subModulosList) {
        this.nombreModulo = nombreModulo;
        this.nombreCtrl = nombreCtrl;
        this.icono = icono;
        this.subModulosList = subModulosList;
    }

    public Integer getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(Integer idModulo) {
        this.idModulo = idModulo;
    }

    public String getNombreModulo() {
        return nombreModulo;
    }

    public void setNombreModulo(String nombreModulo) {
        this.nombreModulo = nombreModulo;
    }

    @XmlTransient
    @JsonIgnore
    public List<SubModulos> getSubModulosList() {
        return subModulosList;
    }

    public void setSubModulosList(List<SubModulos> subModulosList) {
        this.subModulosList = subModulosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idModulo != null ? idModulo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Modulos)) {
            return false;
        }
        Modulos other = (Modulos) object;
        if ((this.idModulo == null && other.idModulo != null) || (this.idModulo != null && !this.idModulo.equals(other.idModulo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.bussineslm.entities.Modulos[ idModulo=" + idModulo + " ]";
    }

    public String getNombreCtrl() {
        return nombreCtrl;
    }

    public void setNombreCtrl(String nombreCtrl) {
        this.nombreCtrl = nombreCtrl;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }
    
}
