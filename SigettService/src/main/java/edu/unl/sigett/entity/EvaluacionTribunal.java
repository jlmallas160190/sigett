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
@Table(name = "evaluacion_tribunal")
@XmlRootElement
public class EvaluacionTribunal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nota")
    private BigDecimal nota;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "observacion")
    private String observacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "sugerencia")
    private String sugerencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "lugar")
    private String lugar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_apto_calificar")
    private Boolean esAptoCalificar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_plazo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPlazo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evaluacionTribunalId")
    private List<CalificacionMiembro> calificacionMiembroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evaluacionTribunalId")
    private List<PlazoEvaluacionTribunal> plazoEvaluacionTribunalList;
    @JoinColumn(name = "tribunal_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tribunal tribunalId;
    @JoinColumn(name = "rango_nota_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RangoNota rangoNotaId;
    @JoinColumn(name = "rango_equivalencia_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RangoEquivalencia rangoEquivalenciaId;
    @Column(name = "catalogo_evaluacion_id")
    private Long catalogoEvaluacionId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evaluacionTribunalId")
    private List<Acta> actaList;

    public EvaluacionTribunal() {
    }

    public EvaluacionTribunal(Long id) {
        this.id = id;
    }

    public EvaluacionTribunal(Long id, BigDecimal nota, String observacion, String sugerencia, String lugar, Date fechaInicio, Date fechaFin,
            Boolean esAptoCalificar, Date fechaPlazo, Tribunal tribunalId, RangoNota rangoNotaId, Long catalogoEvaluacionId) {
        this.id = id;
        this.nota = nota;
        this.observacion = observacion;
        this.sugerencia = sugerencia;
        this.lugar = lugar;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.esAptoCalificar = esAptoCalificar;
        this.fechaPlazo = fechaPlazo;
        this.tribunalId = tribunalId;
        this.rangoNotaId = rangoNotaId;
        this.catalogoEvaluacionId = catalogoEvaluacionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getNota() {
        return nota;
    }

    public void setNota(BigDecimal nota) {
        this.nota = nota;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getSugerencia() {
        return sugerencia;
    }

    public void setSugerencia(String sugerencia) {
        this.sugerencia = sugerencia;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getEsAptoCalificar() {
        return esAptoCalificar;
    }

    public void setEsAptoCalificar(Boolean esAptoCalificar) {
        this.esAptoCalificar = esAptoCalificar;
    }

    public Date getFechaPlazo() {
        return fechaPlazo;
    }

    public void setFechaPlazo(Date fechaPlazo) {
        this.fechaPlazo = fechaPlazo;
    }

    @XmlTransient
    public List<CalificacionMiembro> getCalificacionMiembroList() {
        return calificacionMiembroList;
    }

    public void setCalificacionMiembroList(List<CalificacionMiembro> calificacionMiembroList) {
        this.calificacionMiembroList = calificacionMiembroList;
    }

    @XmlTransient
    public List<PlazoEvaluacionTribunal> getPlazoEvaluacionTribunalList() {
        return plazoEvaluacionTribunalList;
    }

    public void setPlazoEvaluacionTribunalList(List<PlazoEvaluacionTribunal> plazoEvaluacionTribunalList) {
        this.plazoEvaluacionTribunalList = plazoEvaluacionTribunalList;
    }

    public Tribunal getTribunalId() {
        return tribunalId;
    }

    public void setTribunalId(Tribunal tribunalId) {
        this.tribunalId = tribunalId;
    }

    public RangoNota getRangoNotaId() {
        return rangoNotaId;
    }

    public void setRangoNotaId(RangoNota rangoNotaId) {
        this.rangoNotaId = rangoNotaId;
    }

    public RangoEquivalencia getRangoEquivalenciaId() {
        return rangoEquivalenciaId;
    }

    public void setRangoEquivalenciaId(RangoEquivalencia rangoEquivalenciaId) {
        this.rangoEquivalenciaId = rangoEquivalenciaId;
    }

    public Long getCatalogoEvaluacionId() {
        return catalogoEvaluacionId;
    }

    public void setCatalogoEvaluacionId(Long catalogoEvaluacionId) {
        this.catalogoEvaluacionId = catalogoEvaluacionId;
    }

    @XmlTransient
    public List<Acta> getActaList() {
        return actaList;
    }

    public void setActaList(List<Acta> actaList) {
        this.actaList = actaList;
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
        if (!(object instanceof EvaluacionTribunal)) {
            return false;
        }
        EvaluacionTribunal other = (EvaluacionTribunal) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.EvaluacionTribunal[ id=" + id + " ]";
    }

}
