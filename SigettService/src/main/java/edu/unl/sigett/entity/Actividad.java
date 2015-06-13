/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "actividad")
@XmlRootElement
@Cacheable(value = false)
public class Actividad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "duracion")
    private Double duracion;
    @Column(name = "padre_id")
    private Long padreId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_culminacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCulminacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_activo")
    private Boolean esActivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentaje_duracion")
    private BigDecimal porcentajeDuracion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "avance")
    private BigDecimal avance;
    @Basic(optional = false)
    @NotNull
    @Column(name = "faltante")
    private BigDecimal faltante;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "tipo_id")
    private Long tipoId;
    @Column(name = "estado_id")
    @ManyToOne(optional = false)
    private Long estadoId;
    @JoinColumn(name = "cronograma_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Cronograma cronogramaId;
    @OneToMany(mappedBy = "actividadId")
    private List<Revision> revisionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "actividadId")
    private List<DocumentoActividad> documentoActividadList;

    public Actividad() {
    }

    public Actividad(Long id) {
        this.id = id;
    }

    public Actividad(Long id, String nombre, Date fechaInicio, Double duracion, Long actividadId, Boolean esActivo, BigDecimal porcentajeDuracion,
            BigDecimal avance, BigDecimal faltante, String observacion, Long tipoId, Long estadoId) {
        this.id = id;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.duracion = duracion;
        this.padreId = actividadId;
        this.esActivo = esActivo;
        this.porcentajeDuracion = porcentajeDuracion;
        this.avance = avance;
        this.faltante = faltante;
        this.observacion = observacion;
        this.tipoId = tipoId;
        this.estadoId = estadoId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Double getDuracion() {
        return duracion;
    }

    public void setDuracion(Double duracion) {
        this.duracion = duracion;
    }

    public Long getPadreId() {
        return padreId;
    }

    public void setPadreId(Long padreId) {
        this.padreId = padreId;
    }

    public Date getFechaCulminacion() {
        return fechaCulminacion;
    }

    public void setFechaCulminacion(Date fechaCulminacion) {
        this.fechaCulminacion = fechaCulminacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Cronograma getCronogramaId() {
        return cronogramaId;
    }

    public void setCronogramaId(Cronograma cronogramaId) {
        this.cronogramaId = cronogramaId;
    }

    @XmlTransient
    public List<Revision> getRevisionList() {
        return revisionList;
    }

    public void setRevisionList(List<Revision> revisionList) {
        this.revisionList = revisionList;
    }

    @XmlTransient
    public List<DocumentoActividad> getDocumentoActividadList() {
        return documentoActividadList;
    }

    public void setDocumentoActividadList(List<DocumentoActividad> documentoActividadList) {
        this.documentoActividadList = documentoActividadList;
    }

    public Boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(Boolean esActivo) {
        this.esActivo = esActivo;
    }

    public BigDecimal getPorcentajeDuracion() {
        return porcentajeDuracion;
    }

    public void setPorcentajeDuracion(BigDecimal porcentajeDuracion) {
        this.porcentajeDuracion = porcentajeDuracion;
    }

    public BigDecimal getAvance() {
        return avance;
    }

    public void setAvance(BigDecimal avance) {
        this.avance = avance;
    }

    public BigDecimal getFaltante() {
        return faltante;
    }

    public void setFaltante(BigDecimal faltante) {
        this.faltante = faltante;
    }

    public Long getTipoId() {
        return tipoId;
    }

    public void setTipoId(Long tipoId) {
        this.tipoId = tipoId;
    }

    public Long getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Long estadoId) {
        this.estadoId = estadoId;
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
        if (!(object instanceof Actividad)) {
            return false;
        }
        Actividad other = (Actividad) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.Actividad[ id=" + id + " ]";
    }

}
