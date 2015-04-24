/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.entity;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "actividad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Actividad.findAll", query = "SELECT a FROM Actividad a"),
    @NamedQuery(name = "Actividad.findById", query = "SELECT a FROM Actividad a WHERE a.id = :id"),
    @NamedQuery(name = "Actividad.findByNombre", query = "SELECT a FROM Actividad a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "Actividad.findByFechaInicio", query = "SELECT a FROM Actividad a WHERE a.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "Actividad.findByDuracion", query = "SELECT a FROM Actividad a WHERE a.duracion = :duracion"),
    @NamedQuery(name = "Actividad.findByActividadId", query = "SELECT a FROM Actividad a WHERE a.actividadId = :actividadId"),
    @NamedQuery(name = "Actividad.findByFechaCulminacion", query = "SELECT a FROM Actividad a WHERE a.fechaCulminacion = :fechaCulminacion"),
    @NamedQuery(name = "Actividad.findByEsActivo", query = "SELECT a FROM Actividad a WHERE a.esActivo = :esActivo"),
    @NamedQuery(name = "Actividad.findByPorcentajeDuracion", query = "SELECT a FROM Actividad a WHERE a.porcentajeDuracion = :porcentajeDuracion"),
    @NamedQuery(name = "Actividad.findByAvance", query = "SELECT a FROM Actividad a WHERE a.avance = :avance"),
    @NamedQuery(name = "Actividad.findByFaltante", query = "SELECT a FROM Actividad a WHERE a.faltante = :faltante"),
    @NamedQuery(name = "Actividad.findByObservacion", query = "SELECT a FROM Actividad a WHERE a.observacion = :observacion"),
    @NamedQuery(name = "Actividad.findByFechaInicioRevision", query = "SELECT a FROM Actividad a WHERE a.fechaInicioRevision = :fechaInicioRevision"),
    @NamedQuery(name = "Actividad.findByFechaFinRevision", query = "SELECT a FROM Actividad a WHERE a.fechaFinRevision = :fechaFinRevision"),
    @NamedQuery(name = "Actividad.findByLugarRevision", query = "SELECT a FROM Actividad a WHERE a.lugarRevision = :lugarRevision")})
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
    private double duracion;
    @Column(name = "actividad_id")
    private Long actividadId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_culminacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCulminacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_activo")
    private boolean esActivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentaje_duracion")
    private double porcentajeDuracion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "avance")
    private double avance;
    @Basic(optional = false)
    @NotNull
    @Column(name = "faltante")
    private double faltante;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "observacion")
    private String observacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio_revision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicioRevision;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin_revision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFinRevision;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "lugar_revision")
    private String lugarRevision;
    @JoinColumn(name = "tipo_actividad_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TipoActividad tipoActividadId;
    @JoinColumn(name = "estado_actividad_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EstadoActividad estadoActividadId;
    @JoinColumn(name = "cronograma_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Cronograma cronogramaId;
    @OneToMany(mappedBy = "actividadId")
    private List<Revision> revisionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "actividadId")
    private List<DocumentoActividad> documentoActividadList;
    @Transient
    private boolean tienePadre;

    public Actividad() {
    }

    public Actividad(Long id) {
        this.id = id;
    }

    public Actividad(Long id, String nombre, Date fechaInicio, double duracion, Date fechaCulminacion, boolean esActivo, double porcentajeDuracion, double avance, double faltante, String observacion, Date fechaInicioRevision, Date fechaFinRevision, String lugarRevision) {
        this.id = id;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.duracion = duracion;
        this.fechaCulminacion = fechaCulminacion;
        this.esActivo = esActivo;
        this.porcentajeDuracion = porcentajeDuracion;
        this.avance = avance;
        this.faltante = faltante;
        this.observacion = observacion;
        this.fechaInicioRevision = fechaInicioRevision;
        this.fechaFinRevision = fechaFinRevision;
        this.lugarRevision = lugarRevision;
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

    public double getDuracion() {
        return duracion;
    }

    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }

    public Long getActividadId() {
        return actividadId;
    }

    public void setActividadId(Long actividadId) {
        this.actividadId = actividadId;
    }

    public Date getFechaCulminacion() {
        return fechaCulminacion;
    }

    public void setFechaCulminacion(Date fechaCulminacion) {
        this.fechaCulminacion = fechaCulminacion;
    }

    public boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(boolean esActivo) {
        this.esActivo = esActivo;
    }

    public double getPorcentajeDuracion() {
        return porcentajeDuracion;
    }

    public void setPorcentajeDuracion(double porcentajeDuracion) {
        this.porcentajeDuracion = porcentajeDuracion;
    }

    public double getAvance() {
        return avance;
    }

    public void setAvance(double avance) {
        this.avance = avance;
    }

    public double getFaltante() {
        return faltante;
    }

    public void setFaltante(double faltante) {
        this.faltante = faltante;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getFechaInicioRevision() {
        return fechaInicioRevision;
    }

    public void setFechaInicioRevision(Date fechaInicioRevision) {
        this.fechaInicioRevision = fechaInicioRevision;
    }

    public Date getFechaFinRevision() {
        return fechaFinRevision;
    }

    public void setFechaFinRevision(Date fechaFinRevision) {
        this.fechaFinRevision = fechaFinRevision;
    }

    public String getLugarRevision() {
        return lugarRevision;
    }

    public void setLugarRevision(String lugarRevision) {
        this.lugarRevision = lugarRevision;
    }

    public TipoActividad getTipoActividadId() {
        return tipoActividadId;
    }

    public void setTipoActividadId(TipoActividad tipoActividadId) {
        this.tipoActividadId = tipoActividadId;
    }

    public EstadoActividad getEstadoActividadId() {
        return estadoActividadId;
    }

    public void setEstadoActividadId(EstadoActividad estadoActividadId) {
        this.estadoActividadId = estadoActividadId;
    }

    public Cronograma getCronogramaId() {
        return cronogramaId;
    }

    public void setCronogramaId(Cronograma cronogramaId) {
        this.cronogramaId = cronogramaId;
    }

    public boolean isTienePadre() {
        return tienePadre;
    }

    public void setTienePadre(boolean tienePadre) {
        this.tienePadre = tienePadre;
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
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.Actividad[ id=" + id + " ]";
    }

}
