/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bussineslm.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
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
@Table(name = "informacion_laboral")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InformacionLaboral.findAll", query = "SELECT i FROM InformacionLaboral i")
    , @NamedQuery(name = "InformacionLaboral.findByIdPersona", query = "SELECT i FROM InformacionLaboral i WHERE i.idPersona = :idPersona")
    , @NamedQuery(name = "InformacionLaboral.findByFechaInicial", query = "SELECT i FROM InformacionLaboral i WHERE i.fechaInicial = :fechaInicial")
    , @NamedQuery(name = "InformacionLaboral.findByFechaFinal", query = "SELECT i FROM InformacionLaboral i WHERE i.fechaFinal = :fechaFinal")})
public class InformacionLaboral implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_persona")
    private Integer idPersona;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicial")
    @Temporal(TemporalType.DATE)
    private Date fechaInicial;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_final")
    @Temporal(TemporalType.DATE)
    private Date fechaFinal;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "idPersonaLab")
    private ServiciosPorEmpleados serviciosPorEmpleados;
    @JoinColumn(name = "id_cargo", referencedColumnName = "id_cargo")
    @ManyToOne(optional = false)
    private Cargos idCargo;
    @JoinColumn(name = "id_persona", referencedColumnName = "identificacion", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Personas personas;
    @JoinColumn(name = "id_tipo_contrato", referencedColumnName = "id_tipo_contrato")
    @ManyToOne(optional = false)
    private TiposContratos idTipoContrato;

    public InformacionLaboral() {
    }

    public InformacionLaboral(Integer idPersona) {
        this.idPersona = idPersona;
    }

    public InformacionLaboral(Integer idPersona, Date fechaInicial, Date fechaFinal) {
        this.idPersona = idPersona;
        this.fechaInicial = fechaInicial;
        this.fechaFinal = fechaFinal;
    }

    public Integer getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Integer idPersona) {
        this.idPersona = idPersona;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public ServiciosPorEmpleados getServiciosPorEmpleados() {
        return serviciosPorEmpleados;
    }

    public void setServiciosPorEmpleados(ServiciosPorEmpleados serviciosPorEmpleados) {
        this.serviciosPorEmpleados = serviciosPorEmpleados;
    }

    public Cargos getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(Cargos idCargo) {
        this.idCargo = idCargo;
    }

    public Personas getPersonas() {
        return personas;
    }

    public void setPersonas(Personas personas) {
        this.personas = personas;
    }

    public TiposContratos getIdTipoContrato() {
        return idTipoContrato;
    }

    public void setIdTipoContrato(TiposContratos idTipoContrato) {
        this.idTipoContrato = idTipoContrato;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPersona != null ? idPersona.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InformacionLaboral)) {
            return false;
        }
        InformacionLaboral other = (InformacionLaboral) object;
        if ((this.idPersona == null && other.idPersona != null) || (this.idPersona != null && !this.idPersona.equals(other.idPersona))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.bussineslm.entities.InformacionLaboral[ idPersona=" + idPersona + " ]";
    }
    
}
