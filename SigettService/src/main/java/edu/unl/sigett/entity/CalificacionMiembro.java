/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "calificacion_miembro")
@XmlRootElement
@Cacheable(value = false)
public class CalificacionMiembro implements Serializable {

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
    @Size(min = 1, max = 500)
    @Column(name = "observacion")
    private String observacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "miembro_id")
    private String miembroId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_activo")
    private Boolean esActivo;
    @JoinColumn(name = "evaluacion_tribunal_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EvaluacionTribunal evaluacionTribunalId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "calificacionMiembroId")
    private List<CalificacionParametro> calificacionParametroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "calificacionMiembroId")
    private List<SugerenciaCalificacionMiembro> sugerenciaCalificacionMiembroList;
    @Transient
    private Boolean editar;

    public CalificacionMiembro() {
    }

    public CalificacionMiembro(Long id) {
        this.id = id;
    }

    public CalificacionMiembro(Long id, BigDecimal nota, String observacion, String miembroId, Boolean esActivo, EvaluacionTribunal evaluacionTribunalId) {
        this.id = id;
        this.nota = nota;
        this.observacion = observacion;
        this.miembroId = miembroId;
        this.esActivo = esActivo;
        this.evaluacionTribunalId = evaluacionTribunalId;
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

    public Boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(Boolean esActivo) {
        this.esActivo = esActivo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getMiembroId() {
        return miembroId;
    }

    public void setMiembroId(String miembroId) {
        this.miembroId = miembroId;
    }

    public EvaluacionTribunal getEvaluacionTribunalId() {
        return evaluacionTribunalId;
    }

    public void setEvaluacionTribunalId(EvaluacionTribunal evaluacionTribunalId) {
        this.evaluacionTribunalId = evaluacionTribunalId;
    }

    @XmlTransient
    public List<CalificacionParametro> getCalificacionParametroList() {
        return calificacionParametroList;
    }

    public void setCalificacionParametroList(List<CalificacionParametro> calificacionParametroList) {
        this.calificacionParametroList = calificacionParametroList;
    }

    @XmlTransient
    public List<SugerenciaCalificacionMiembro> getSugerenciaCalificacionMiembroList() {
        return sugerenciaCalificacionMiembroList;
    }

    public void setSugerenciaCalificacionMiembroList(List<SugerenciaCalificacionMiembro> sugerenciaCalificacionMiembroList) {
        this.sugerenciaCalificacionMiembroList = sugerenciaCalificacionMiembroList;
    }

    public Boolean getEditar() {
        return editar;
    }

    public void setEditar(Boolean editar) {
        this.editar = editar;
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
        if (!(object instanceof CalificacionMiembro)) {
            return false;
        }
        CalificacionMiembro other = (CalificacionMiembro) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.CalificacionMiembro[ id=" + id + " ]";
    }

}
