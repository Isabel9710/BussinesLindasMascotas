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
@Table(name = "turnos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Turnos.findAll", query = "SELECT t FROM Turnos t")
    , @NamedQuery(name = "Turnos.findByIdTurno", query = "SELECT t FROM Turnos t WHERE t.idTurno = :idTurno")
    , @NamedQuery(name = "Turnos.findByNombreTurno", query = "SELECT t FROM Turnos t WHERE t.nombreTurno = :nombreTurno")})
public class Turnos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_turno")
    private Integer idTurno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = " nombre_turno")
    private String nombreTurno;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTurno")
    private List<DetallesTurnos> detallesTurnosList;

    public Turnos() {
    }

    public Turnos(Integer idTurno) {
        this.idTurno = idTurno;
    }

    public Turnos(Integer idTurno, String nombreTurno) {
        this.idTurno = idTurno;
        this.nombreTurno = nombreTurno;
    }

    public Integer getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(Integer idTurno) {
        this.idTurno = idTurno;
    }

    public String getNombreTurno() {
        return nombreTurno;
    }

    public void setNombreTurno(String nombreTurno) {
        this.nombreTurno = nombreTurno;
    }

    @XmlTransient
    @JsonIgnore
    public List<DetallesTurnos> getDetallesTurnosList() {
        return detallesTurnosList;
    }

    public void setDetallesTurnosList(List<DetallesTurnos> detallesTurnosList) {
        this.detallesTurnosList = detallesTurnosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTurno != null ? idTurno.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Turnos)) {
            return false;
        }
        Turnos other = (Turnos) object;
        if ((this.idTurno == null && other.idTurno != null) || (this.idTurno != null && !this.idTurno.equals(other.idTurno))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.bussineslm.entities.Turnos[ idTurno=" + idTurno + " ]";
    }
    
}
