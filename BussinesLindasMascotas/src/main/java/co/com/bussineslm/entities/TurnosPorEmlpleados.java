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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Isabel Medina
 */
@Entity
@Table(name = "turnos_por_emlpleados")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TurnosPorEmlpleados.findAll", query = "SELECT t FROM TurnosPorEmlpleados t")
    , @NamedQuery(name = "TurnosPorEmlpleados.findByIdTunosPorEmpl", query = "SELECT t FROM TurnosPorEmlpleados t WHERE t.idTunosPorEmpl = :idTunosPorEmpl")
    , @NamedQuery(name = "TurnosPorEmlpleados.findByIdPersonaLab", query = "SELECT t FROM TurnosPorEmlpleados t WHERE t.idPersonaLab = :idPersonaLab")
    , @NamedQuery(name = "TurnosPorEmlpleados.findByIdDetalleTurno", query = "SELECT t FROM TurnosPorEmlpleados t WHERE t.idDetalleTurno = :idDetalleTurno")})
public class TurnosPorEmlpleados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tunos_por_empl")
    private Integer idTunosPorEmpl;
    @Basic(optional = false)
    @Column(name = "id_persona_lab")
    private int idPersonaLab;
    @Basic(optional = false)
    @Column(name = "id_detalle_turno")
    private int idDetalleTurno;

    public TurnosPorEmlpleados() {
    }

    public TurnosPorEmlpleados(Integer idTunosPorEmpl) {
        this.idTunosPorEmpl = idTunosPorEmpl;
    }

    public TurnosPorEmlpleados(Integer idTunosPorEmpl, int idPersonaLab, int idDetalleTurno) {
        this.idTunosPorEmpl = idTunosPorEmpl;
        this.idPersonaLab = idPersonaLab;
        this.idDetalleTurno = idDetalleTurno;
    }

    public Integer getIdTunosPorEmpl() {
        return idTunosPorEmpl;
    }

    public void setIdTunosPorEmpl(Integer idTunosPorEmpl) {
        this.idTunosPorEmpl = idTunosPorEmpl;
    }

    public int getIdPersonaLab() {
        return idPersonaLab;
    }

    public void setIdPersonaLab(int idPersonaLab) {
        this.idPersonaLab = idPersonaLab;
    }

    public int getIdDetalleTurno() {
        return idDetalleTurno;
    }

    public void setIdDetalleTurno(int idDetalleTurno) {
        this.idDetalleTurno = idDetalleTurno;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTunosPorEmpl != null ? idTunosPorEmpl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TurnosPorEmlpleados)) {
            return false;
        }
        TurnosPorEmlpleados other = (TurnosPorEmlpleados) object;
        if ((this.idTunosPorEmpl == null && other.idTunosPorEmpl != null) || (this.idTunosPorEmpl != null && !this.idTunosPorEmpl.equals(other.idTunosPorEmpl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.bussineslm.entities.TurnosPorEmlpleados[ idTunosPorEmpl=" + idTunosPorEmpl + " ]";
    }
    
}
