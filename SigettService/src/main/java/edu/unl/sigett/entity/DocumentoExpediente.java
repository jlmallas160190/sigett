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
@Table(name = "documento_expediente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentoExpediente.findAll", query = "SELECT d FROM DocumentoExpediente d"),
    @NamedQuery(name = "DocumentoExpediente.findById", query = "SELECT d FROM DocumentoExpediente d WHERE d.id = :id"),
    @NamedQuery(name = "DocumentoExpediente.findByFecha", query = "SELECT d FROM DocumentoExpediente d WHERE d.fecha = :fecha"),
    @NamedQuery(name = "DocumentoExpediente.findByTamanio", query = "SELECT d FROM DocumentoExpediente d WHERE d.tamanio = :tamanio"),
    @NamedQuery(name = "DocumentoExpediente.findByDescripcion", query = "SELECT d FROM DocumentoExpediente d WHERE d.descripcion = :descripcion"),
    @NamedQuery(name = "DocumentoExpediente.findByTipoArchivo", query = "SELECT d FROM DocumentoExpediente d WHERE d.tipoArchivo = :tipoArchivo")})
public class DocumentoExpediente implements Serializable {
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
    @Size(min = 1, max = 500)
    @Column(name = "descripcion")
    private String descripcion;
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
    @JoinColumn(name = "expediente_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Expediente expedienteId;
    @JoinColumn(name = "catalogo_documento_expediente_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CatalogoDocumentoExpediente catalogoDocumentoExpedienteId;

    public DocumentoExpediente() {
    }

    public DocumentoExpediente(Long id) {
        this.id = id;
    }

    public DocumentoExpediente(Long id, Date fecha, double tamanio, String descripcion, String tipoArchivo, byte[] documento) {
        this.id = id;
        this.fecha = fecha;
        this.tamanio = tamanio;
        this.descripcion = descripcion;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public Expediente getExpedienteId() {
        return expedienteId;
    }

    public void setExpedienteId(Expediente expedienteId) {
        this.expedienteId = expedienteId;
    }

    public CatalogoDocumentoExpediente getCatalogoDocumentoExpedienteId() {
        return catalogoDocumentoExpedienteId;
    }

    public void setCatalogoDocumentoExpedienteId(CatalogoDocumentoExpediente catalogoDocumentoExpedienteId) {
        this.catalogoDocumentoExpedienteId = catalogoDocumentoExpedienteId;
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
        if (!(object instanceof DocumentoExpediente)) {
            return false;
        }
        DocumentoExpediente other = (DocumentoExpediente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.DocumentoExpediente[ id=" + id + " ]";
    }
    
}
