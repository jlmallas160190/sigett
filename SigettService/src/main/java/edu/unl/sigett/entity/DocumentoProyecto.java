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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "documento_proyecto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentoProyecto.findAll", query = "SELECT d FROM DocumentoProyecto d"),
    @NamedQuery(name = "DocumentoProyecto.findById", query = "SELECT d FROM DocumentoProyecto d WHERE d.id = :id"),
    @NamedQuery(name = "DocumentoProyecto.findByFecha", query = "SELECT d FROM DocumentoProyecto d WHERE d.fecha = :fecha"),
    @NamedQuery(name = "DocumentoProyecto.findByTamanio", query = "SELECT d FROM DocumentoProyecto d WHERE d.tamanio = :tamanio"),
    @NamedQuery(name = "DocumentoProyecto.findByEsActivo", query = "SELECT d FROM DocumentoProyecto d WHERE d.esActivo = :esActivo"),
    @NamedQuery(name = "DocumentoProyecto.findByTipoArchivo", query = "SELECT d FROM DocumentoProyecto d WHERE d.tipoArchivo = :tipoArchivo")})
public class DocumentoProyecto implements Serializable {

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
    @Column(name = "es_activo")
    private boolean esActivo;
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
    @JoinColumn(name = "proyecto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Proyecto proyectoId;
    @JoinColumn(name = "catalogo_documento_proyecto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CatalogoDocumentoProyecto catalogoDocumentoProyectoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "documentoProyectoId")
    private List<EstructuraDocumentoProyecto> estructuraDocumentoProyectoList;
    @Transient
    private boolean esEditado;

    public DocumentoProyecto() {
        this.esEditado = false;
    }

    public DocumentoProyecto(Long id) {
        this.id = id;
    }

    public DocumentoProyecto(Long id, Date fecha, double tamanio, boolean esActivo, String tipoArchivo, byte[] documento) {
        this.id = id;
        this.fecha = fecha;
        this.tamanio = tamanio;
        this.esActivo = esActivo;
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

    public boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(boolean esActivo) {
        this.esActivo = esActivo;
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

    public Proyecto getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Proyecto proyectoId) {
        this.proyectoId = proyectoId;
    }

    public CatalogoDocumentoProyecto getCatalogoDocumentoProyectoId() {
        return catalogoDocumentoProyectoId;
    }

    public void setCatalogoDocumentoProyectoId(CatalogoDocumentoProyecto catalogoDocumentoProyectoId) {
        this.catalogoDocumentoProyectoId = catalogoDocumentoProyectoId;
    }

    public boolean isEsEditado() {
        return esEditado;
    }

    public void setEsEditado(boolean esEditado) {
        this.esEditado = esEditado;
    }

    @XmlTransient
    public List<EstructuraDocumentoProyecto> getEstructuraDocumentoProyectoList() {
        return estructuraDocumentoProyectoList;
    }

    public void setEstructuraDocumentoProyectoList(List<EstructuraDocumentoProyecto> estructuraDocumentoProyectoList) {
        this.estructuraDocumentoProyectoList = estructuraDocumentoProyectoList;
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
        if (!(object instanceof DocumentoProyecto)) {
            return false;
        }
        DocumentoProyecto other = (DocumentoProyecto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.DocumentoProyecto[ id=" + id + " ]";
    }

}
