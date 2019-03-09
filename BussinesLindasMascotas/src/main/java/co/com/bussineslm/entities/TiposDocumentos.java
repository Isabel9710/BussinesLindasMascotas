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
@Table(name = "tipos_documentos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TiposDocumentos.findAll", query = "SELECT t FROM TiposDocumentos t")
    , @NamedQuery(name = "TiposDocumentos.findByIdTipoDoc", query = "SELECT t FROM TiposDocumentos t WHERE t.idTipoDoc = :idTipoDoc")
    , @NamedQuery(name = "TiposDocumentos.findByNombreTipoDoc", query = "SELECT t FROM TiposDocumentos t WHERE t.nombreTipoDoc = :nombreTipoDoc")})
public class TiposDocumentos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipo_doc")
    private Integer idTipoDoc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombre_tipo_doc")
    private String nombreTipoDoc;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoDocumento")
    private List<Personas> personasList;

    public TiposDocumentos() {
    }

    public TiposDocumentos(Integer idTipoDoc) {
        this.idTipoDoc = idTipoDoc;
    }

    public TiposDocumentos(Integer idTipoDoc, String nombreTipoDoc) {
        this.idTipoDoc = idTipoDoc;
        this.nombreTipoDoc = nombreTipoDoc;
    }

    public Integer getIdTipoDoc() {
        return idTipoDoc;
    }

    public void setIdTipoDoc(Integer idTipoDoc) {
        this.idTipoDoc = idTipoDoc;
    }

    public String getNombreTipoDoc() {
        return nombreTipoDoc;
    }

    public void setNombreTipoDoc(String nombreTipoDoc) {
        this.nombreTipoDoc = nombreTipoDoc;
    }

    @XmlTransient
    @JsonIgnore
    public List<Personas> getPersonasList() {
        return personasList;
    }

    public void setPersonasList(List<Personas> personasList) {
        this.personasList = personasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoDoc != null ? idTipoDoc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TiposDocumentos)) {
            return false;
        }
        TiposDocumentos other = (TiposDocumentos) object;
        if ((this.idTipoDoc == null && other.idTipoDoc != null) || (this.idTipoDoc != null && !this.idTipoDoc.equals(other.idTipoDoc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.bussineslm.entities.TiposDocumentos[ idTipoDoc=" + idTipoDoc + " ]";
    }
    
}
