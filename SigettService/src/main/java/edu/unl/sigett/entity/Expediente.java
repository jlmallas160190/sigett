/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "expediente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Expediente.findAll", query = "SELECT e FROM Expediente e"),
    @NamedQuery(name = "Expediente.findById", query = "SELECT e FROM Expediente e WHERE e.id = :id"),
    @NamedQuery(name = "Expediente.findByFecha", query = "SELECT e FROM Expediente e WHERE e.fecha = :fecha"),
    @NamedQuery(name = "Expediente.findByObservacion", query = "SELECT e FROM Expediente e WHERE e.observacion = :observacion"),
    @NamedQuery(name = "Expediente.findByNombre", query = "SELECT e FROM Expediente e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "Expediente.findByEsActivo", query = "SELECT e FROM Expediente e WHERE e.esActivo = :esActivo")})
public class Expediente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "observacion")
    private String observacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_activo")
    private boolean esActivo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expedienteId")
    private List<DocumentoExpediente> documentoExpedienteList;
    @JoinColumn(name = "autor_proyecto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AutorProyecto autorProyectoId;

    public Expediente() {
    }

    public Expediente(Long id) {
        this.id = id;
    }

    public Expediente(Long id, Date fecha, String observacion, String nombre, boolean esActivo) {
        this.id = id;
        this.fecha = fecha;
        this.observacion = observacion;
        this.nombre = nombre;
        this.esActivo = esActivo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(boolean esActivo) {
        this.esActivo = esActivo;
    }

    @XmlTransient
    public List<DocumentoExpediente> getDocumentoExpedienteList() {
        return documentoExpedienteList;
    }

    public void setDocumentoExpedienteList(List<DocumentoExpediente> documentoExpedienteList) {
        this.documentoExpedienteList = documentoExpedienteList;
    }

    public AutorProyecto getAutorProyectoId() {
        return autorProyectoId;
    }

    public void setAutorProyectoId(AutorProyecto autorProyectoId) {
        this.autorProyectoId = autorProyectoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Expediente)) {
            return false;
        }
        Expediente other = (Expediente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.Expediente[ id=" + id + " ]";
    }
    
}
