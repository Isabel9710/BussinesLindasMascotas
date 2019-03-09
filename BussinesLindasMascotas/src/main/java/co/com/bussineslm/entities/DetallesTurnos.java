/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bussineslm.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Isabel Medina
 */
@Entity
@Table(name = "detalles_turnos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetallesTurnos.findAll", query = "SELECT d FROM DetallesTurnos d")
    , @NamedQuery(name = "DetallesTurnos.findByIdDetalleTurno", query = "SELECT d FROM DetallesTurnos d WHERE d.idDetalleTurno = :idDetalleTurno")
    , @NamedQuery(name = "DetallesTurnos.findByDias", query = "SELECT d FROM DetallesTurnos d WHERE d.dias = :dias")
    , @NamedQuery(name = "DetallesTurnos.findByHoraInicial", query = "SELECT d FROM DetallesTurnos d WHERE d.horaInicial = :horaInicial")
    , @NamedQuery(name = "DetallesTurnos.findByHoraFinal", query = "SELECT d FROM DetallesTurnos d WHERE d.horaFinal = :horaFinal")})
public class DetallesTurnos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_detalle_turno")
    private Integer idDetalleTurno;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dias")
    private int dias;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hora_inicial")
    @Temporal(TemporalType.TIME)
    private Date horaInicial;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hora_final")
    @Temporal(TemporalType.TIME)
    private Date horaFinal;
    @JoinColumn(name = "id_turno", referencedColumnName = "id_turno")
    @ManyToOne(optional = false)
    private Turnos idTurno;

    public DetallesTurnos() {
    }

    public DetallesTurnos(Integer idDetalleTurno) {
        this.idDetalleTurno = idDetalleTurno;
    }

    public DetallesTurnos(Integer idDetalleTurno, int dias, Date horaInicial, Date horaFinal) {
        this.idDetalleTurno = idDetalleTurno;
        this.dias = dias;
        this.horaInicial = horaInicial;
        this.horaFinal = horaFinal;
    }

    public Integer getIdDetalleTurno() {
        return idDetalleTurno;
    }

    public void setIdDetalleTurno(Integer idDetalleTurno) {
        this.idDetalleTurno = idDetalleTurno;
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public Date getHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(Date horaInicial) {
        this.horaInicial = horaInicial;
    }

    public Date getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(Date horaFinal) {
        this.horaFinal = horaFinal;
    }

    public Turnos getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(Turnos idTurno) {
        this.idTurno = idTurno;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalleTurno != null ? idDetalleTurno.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetallesTurnos)) {
            return false;
        }
        DetallesTurnos other = (DetallesTurnos) object;
        if ((this.idDetalleTurno == null && other.idDetalleTurno != null) || (this.idDetalleTurno != null && !this.idDetalleTurno.equals(other.idDetalleTurno))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.bussineslm.entities.DetallesTurnos[ idDetalleTurno=" + idDetalleTurno + " ]";
    }
    
}
