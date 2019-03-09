/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bussineslm.entities;

import java.io.Serializable;
import javax.persistence.Basic;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Isabel Medina
 */
@Entity
@Table(name = "permisos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Permisos.findAll", query = "SELECT p FROM Permisos p")
    , @NamedQuery(name = "Permisos.findByIdPermiso", query = "SELECT p FROM Permisos p WHERE p.idPermiso = :idPermiso")
    , @NamedQuery(name = "Permisos.findByCrear", query = "SELECT p FROM Permisos p WHERE p.crear = :crear")
    , @NamedQuery(name = "Permisos.findByEditar", query = "SELECT p FROM Permisos p WHERE p.editar = :editar")
    , @NamedQuery(name = "Permisos.findByEliminar", query = "SELECT p FROM Permisos p WHERE p.eliminar = :eliminar")
    , @NamedQuery(name = "Permisos.findByDetalles", query = "SELECT p FROM Permisos p WHERE p.detalles = :detalles")})
public class Permisos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_permiso")
    private Integer idPermiso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "crear")
    private boolean crear;
    @Basic(optional = false)
    @NotNull
    @Column(name = "editar")
    private boolean editar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "eliminar")
    private boolean eliminar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "detalles")
    private boolean detalles;
    @JoinColumn(name = "id_perfil", referencedColumnName = "id_perfil")
    @OneToOne(optional = false)
    private Perfiles idPerfil;
    @JoinColumn(name = "id_submodulo", referencedColumnName = "id_submodulo")
    @ManyToOne(optional = false)
    private SubModulos idSubmodulo;

    public Permisos() {
    }

    public Permisos(Integer idPermiso) {
        this.idPermiso = idPermiso;
    }

    public Permisos(Integer idPermiso, boolean crear, boolean editar, boolean eliminar, boolean detalles) {
        this.idPermiso = idPermiso;
        this.crear = crear;
        this.editar = editar;
        this.eliminar = eliminar;
        this.detalles = detalles;
    }

    public Integer getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(Integer idPermiso) {
        this.idPermiso = idPermiso;
    }

    public boolean getCrear() {
        return crear;
    }

    public void setCrear(boolean crear) {
        this.crear = crear;
    }

    public boolean getEditar() {
        return editar;
    }

    public void setEditar(boolean editar) {
        this.editar = editar;
    }

    public boolean getEliminar() {
        return eliminar;
    }

    public void setEliminar(boolean eliminar) {
        this.eliminar = eliminar;
    }

    public boolean getDetalles() {
        return detalles;
    }

    public void setDetalles(boolean detalles) {
        this.detalles = detalles;
    }

    public Perfiles getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Perfiles idPerfil) {
        this.idPerfil = idPerfil;
    }

    public SubModulos getIdSubmodulo() {
        return idSubmodulo;
    }

    public void setIdSubmodulo(SubModulos idSubmodulo) {
        this.idSubmodulo = idSubmodulo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPermiso != null ? idPermiso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permisos)) {
            return false;
        }
        Permisos other = (Permisos) object;
        if ((this.idPermiso == null && other.idPermiso != null) || (this.idPermiso != null && !this.idPermiso.equals(other.idPermiso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.bussineslm.entities.Permisos[ idPermiso=" + idPermiso + " ]";
    }
    
}
