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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "sub_modulos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SubModulos.findAll", query = "SELECT s FROM SubModulos s")
    , @NamedQuery(name = "SubModulos.findByIdSubmodulo", query = "SELECT s FROM SubModulos s WHERE s.idSubmodulo = :idSubmodulo")
    , @NamedQuery(name = "SubModulos.findByNombreSubmodulo", query = "SELECT s FROM SubModulos s WHERE s.nombreSubmodulo = :nombreSubmodulo")})
public class SubModulos implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "nombreCtrl")
    private String nombreCtrl;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_submodulo")
    private Integer idSubmodulo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombre_submodulo")
    private String nombreSubmodulo;
    @JoinColumn(name = "id_modulo", referencedColumnName = "id_modulo")
    @ManyToOne(optional = false)
    private Modulos idModulo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSubmodulo")
    private List<Permisos> permisosList;

    public SubModulos() {
    }

    public SubModulos(Integer idSubmodulo) {
        this.idSubmodulo = idSubmodulo;
    }

    public SubModulos(String nombreSubmodulo, String nombreCtrl) {
        this.nombreSubmodulo = nombreSubmodulo;
        this.nombreCtrl = nombreCtrl;
    }

    public Integer getIdSubmodulo() {
        return idSubmodulo;
    }

    public void setIdSubmodulo(Integer idSubmodulo) {
        this.idSubmodulo = idSubmodulo;
    }

    public String getNombreSubmodulo() {
        return nombreSubmodulo;
    }

    public void setNombreSubmodulo(String nombreSubmodulo) {
        this.nombreSubmodulo = nombreSubmodulo;
    }

    public Modulos getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(Modulos idModulo) {
        this.idModulo = idModulo;
    }

    @XmlTransient
    @JsonIgnore
    public List<Permisos> getPermisosList() {
        return permisosList;
    }

    public void setPermisosList(List<Permisos> permisosList) {
        this.permisosList = permisosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSubmodulo != null ? idSubmodulo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SubModulos)) {
            return false;
        }
        SubModulos other = (SubModulos) object;
        if ((this.idSubmodulo == null && other.idSubmodulo != null) || (this.idSubmodulo != null && !this.idSubmodulo.equals(other.idSubmodulo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.bussineslm.entities.SubModulos[ idSubmodulo=" + idSubmodulo + " ]";
    }

    public String getNombreCtrl() {
        return nombreCtrl;
    }

    public void setNombreCtrl(String nombreCtrl) {
        this.nombreCtrl = nombreCtrl;
    }
    
}
