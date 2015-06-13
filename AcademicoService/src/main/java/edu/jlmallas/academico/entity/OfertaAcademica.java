/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.entity;

import java.io.Serializable;
import java.util.ArrayList;
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
@Table(name = "oferta_academica", schema = "academico")
@XmlRootElement
@Cacheable(value = false)
public class OfertaAcademica implements Serializable {

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
    @Size(min = 1, max = 15)
    @Column(name = "id_sga")
    private String idSga;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @JoinColumn(name = "periodo_academico_id", referencedColumnName = "id")
    @ManyToOne
    private PeriodoAcademico periodoAcademicoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ofertaAcademicaId")
    private List<ReporteMatricula> reporteMatriculaList;

    public OfertaAcademica() {
        this.reporteMatriculaList = new ArrayList<>();
    }

    public OfertaAcademica(Long id) {
        this.id = id;
    }

    public OfertaAcademica(Long id, String nombre, String idSga, Date fechaInicio, Date fechaFin) {
        this.id = id;
        this.nombre = nombre;
        this.idSga = idSga;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
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

    public String getIdSga() {
        return idSga;
    }

    public void setIdSga(String idSga) {
        this.idSga = idSga;
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

    public PeriodoAcademico getPeriodoAcademicoId() {
        return periodoAcademicoId;
    }

    public void setPeriodoAcademicoId(PeriodoAcademico periodoAcademicoId) {
        this.periodoAcademicoId = periodoAcademicoId;
    }

    @XmlTransient
    public List<ReporteMatricula> getReporteMatriculaList() {
        return reporteMatriculaList;
    }

    public void setReporteMatriculaList(List<ReporteMatricula> reporteMatriculaList) {
        this.reporteMatriculaList = reporteMatriculaList;
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
        if (!(object instanceof OfertaAcademica)) {
            return false;
        }
        OfertaAcademica other = (OfertaAcademica) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return this.nombre;
    }

}
