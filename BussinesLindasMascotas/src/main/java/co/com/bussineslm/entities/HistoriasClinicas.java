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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Isabel Medina
 */
@Entity
@Table(name = "historias_clinicas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HistoriasClinicas.findAll", query = "SELECT h FROM HistoriasClinicas h")
    , @NamedQuery(name = "HistoriasClinicas.findByIdProcedimiento", query = "SELECT h FROM HistoriasClinicas h WHERE h.idProcedimiento = :idProcedimiento")
    , @NamedQuery(name = "HistoriasClinicas.findByDescripcion", query = "SELECT h FROM HistoriasClinicas h WHERE h.descripcion = :descripcion")
    , @NamedQuery(name = "HistoriasClinicas.findByPeso", query = "SELECT h FROM HistoriasClinicas h WHERE h.peso = :peso")
    , @NamedQuery(name = "HistoriasClinicas.findByFechaProcedimiento", query = "SELECT h FROM HistoriasClinicas h WHERE h.fechaProcedimiento = :fechaProcedimiento")})
public class HistoriasClinicas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_procedimiento")
    private Integer idProcedimiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "peso")
    private short peso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_procedimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaProcedimiento;
    @JoinColumn(name = "id_mascota", referencedColumnName = "id_mascota")
    @ManyToOne(optional = false)
    private Mascotas idMascota;
    @JoinColumn(name = "id_servicio_por_empl", referencedColumnName = "id_servicio_empl")
    @ManyToOne(optional = false)
    private ServiciosPorEmpleados idServicioPorEmpl;

    public HistoriasClinicas() {
    }

    public HistoriasClinicas(Integer idProcedimiento) {
        this.idProcedimiento = idProcedimiento;
    }

    public HistoriasClinicas(Integer idProcedimiento, String descripcion, short peso, Date fechaProcedimiento) {
        this.idProcedimiento = idProcedimiento;
        this.descripcion = descripcion;
        this.peso = peso;
        this.fechaProcedimiento = fechaProcedimiento;
    }

    public Integer getIdProcedimiento() {
        return idProcedimiento;
    }

    public void setIdProcedimiento(Integer idProcedimiento) {
        this.idProcedimiento = idProcedimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public short getPeso() {
        return peso;
    }

    public void setPeso(short peso) {
        this.peso = peso;
    }

    public Date getFechaProcedimiento() {
        return fechaProcedimiento;
    }

    public void setFechaProcedimiento(Date fechaProcedimiento) {
        this.fechaProcedimiento = fechaProcedimiento;
    }

    public Mascotas getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(Mascotas idMascota) {
        this.idMascota = idMascota;
    }

    public ServiciosPorEmpleados getIdServicioPorEmpl() {
        return idServicioPorEmpl;
    }

    public void setIdServicioPorEmpl(ServiciosPorEmpleados idServicioPorEmpl) {
        this.idServicioPorEmpl = idServicioPorEmpl;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProcedimiento != null ? idProcedimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistoriasClinicas)) {
            return false;
        }
        HistoriasClinicas other = (HistoriasClinicas) object;
        if ((this.idProcedimiento == null && other.idProcedimiento != null) || (this.idProcedimiento != null && !this.idProcedimiento.equals(other.idProcedimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.bussineslm.entities.HistoriasClinicas[ idProcedimiento=" + idProcedimiento + " ]";
    }
    
}
