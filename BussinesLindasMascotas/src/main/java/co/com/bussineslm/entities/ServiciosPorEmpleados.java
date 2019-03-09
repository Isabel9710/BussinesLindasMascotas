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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Isabel Medina
 */
@Entity
@Table(name = "servicios_por_empleados")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ServiciosPorEmpleados.findAll", query = "SELECT s FROM ServiciosPorEmpleados s")
    , @NamedQuery(name = "ServiciosPorEmpleados.findByIdServicioEmpl", query = "SELECT s FROM ServiciosPorEmpleados s WHERE s.idServicioEmpl = :idServicioEmpl")})
public class ServiciosPorEmpleados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_servicio_empl")
    private Integer idServicioEmpl;
    @JoinColumn(name = "id_persona_lab", referencedColumnName = "id_persona")
    @OneToOne(optional = false)
    private InformacionLaboral idPersonaLab;
    @JoinColumn(name = "id_servicio", referencedColumnName = "id_servicio")
    @OneToOne(optional = false)
    private Servicios idServicio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idServicioPorEmpl")
    private List<Citas> citasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idServicioPorEmpl")
    private List<HistoriasClinicas> historiasClinicasList;

    public ServiciosPorEmpleados() {
    }

    public ServiciosPorEmpleados(Integer idServicioEmpl) {
        this.idServicioEmpl = idServicioEmpl;
    }

    public Integer getIdServicioEmpl() {
        return idServicioEmpl;
    }

    public void setIdServicioEmpl(Integer idServicioEmpl) {
        this.idServicioEmpl = idServicioEmpl;
    }

    public InformacionLaboral getIdPersonaLab() {
        return idPersonaLab;
    }

    public void setIdPersonaLab(InformacionLaboral idPersonaLab) {
        this.idPersonaLab = idPersonaLab;
    }

    public Servicios getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Servicios idServicio) {
        this.idServicio = idServicio;
    }

    @XmlTransient
    @JsonIgnore
    public List<Citas> getCitasList() {
        return citasList;
    }

    public void setCitasList(List<Citas> citasList) {
        this.citasList = citasList;
    }

    @XmlTransient
    @JsonIgnore
    public List<HistoriasClinicas> getHistoriasClinicasList() {
        return historiasClinicasList;
    }

    public void setHistoriasClinicasList(List<HistoriasClinicas> historiasClinicasList) {
        this.historiasClinicasList = historiasClinicasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idServicioEmpl != null ? idServicioEmpl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServiciosPorEmpleados)) {
            return false;
        }
        ServiciosPorEmpleados other = (ServiciosPorEmpleados) object;
        if ((this.idServicioEmpl == null && other.idServicioEmpl != null) || (this.idServicioEmpl != null && !this.idServicioEmpl.equals(other.idServicioEmpl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.bussineslm.entities.ServiciosPorEmpleados[ idServicioEmpl=" + idServicioEmpl + " ]";
    }
    
}
