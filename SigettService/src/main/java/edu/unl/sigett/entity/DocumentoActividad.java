/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
 * @author jorge-luis
 */
@Entity
@Table(name = "documento_actividad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentoActividad.findAll", query = "SELECT d FROM DocumentoActividad d"),
    @NamedQuery(name = "DocumentoActividad.findById", query = "SELECT d FROM DocumentoActividad d WHERE d.id = :id"),
    @NamedQuery(name = "DocumentoActividad.findByFecha", query = "SELECT d FROM DocumentoActividad d WHERE d.fecha = :fecha"),
    @NamedQuery(name = "DocumentoActividad.findByTamanio", query = "SELECT d FROM DocumentoActividad d WHERE d.tamanio = :tamanio"),
    @NamedQuery(name = "DocumentoActividad.findByEsActual", query = "SELECT d FROM DocumentoActividad d WHERE d.esActual = :esActual"),
    @NamedQuery(name = "DocumentoActividad.findByTipoArchivo", query = "SELECT d FROM DocumentoActividad d WHERE d.tipoArchivo = :tipoArchivo")})
public class DocumentoActividad implements Serializable {
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
    @Column(name = "tamanio")
    private double tamanio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_actual")
    private boolean esActual;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "tipo_archivo")
    private String tipoArchivo;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "documento")
    private byte[] documento;
    @JoinColumn(name = "actividad_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Actividad actividadId;

    public DocumentoActividad() {
    }

    public DocumentoActividad(Long id) {
        this.id = id;
    }

    public DocumentoActividad(Long id, Date fecha, double tamanio, boolean esActual, String tipoArchivo, byte[] documento) {
        this.id = id;
        this.fecha = fecha;
        this.tamanio = tamanio;
        this.esActual = esActual;
        this.tipoArchivo = tipoArchivo;
        this.documento = documento;
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

    public double getTamanio() {
        return tamanio;
    }

    public void setTamanio(double tamanio) {
        this.tamanio = tamanio;
    }

    public boolean getEsActual() {
        return esActual;
    }

    public void setEsActual(boolean esActual) {
        this.esActual = esActual;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public byte[] getDocumento() {
        return documento;
    }

    public void setDocumento(byte[] documento) {
        this.documento = documento;
    }

    public Actividad getActividadId() {
        return actividadId;
    }

    public void setActividadId(Actividad actividadId) {
        this.actividadId = actividadId;
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
        if (!(object instanceof DocumentoActividad)) {
            return false;
        }
        DocumentoActividad other = (DocumentoActividad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.DocumentoActividad[ id=" + id + " ]";
    }
    
}
