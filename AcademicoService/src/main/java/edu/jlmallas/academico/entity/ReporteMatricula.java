/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.entity;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "reporte_matricula", schema = "academico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReporteMatricula.findAll", query = "SELECT r FROM ReporteMatricula r"),
    @NamedQuery(name = "ReporteMatricula.findById", query = "SELECT r FROM ReporteMatricula r WHERE r.id = :id"),
    @NamedQuery(name = "ReporteMatricula.findByMatriculaId", query = "SELECT r FROM ReporteMatricula r WHERE r.matriculaId = :matriculaId"),
    @NamedQuery(name = "ReporteMatricula.findByModuloMatriculado", query = "SELECT r FROM ReporteMatricula r WHERE r.moduloMatriculado = :moduloMatriculado"),
    @NamedQuery(name = "ReporteMatricula.findByNumeroModuloMatriculado", query = "SELECT r FROM ReporteMatricula r WHERE r.numeroModuloMatriculado = :numeroModuloMatriculado"),
    @NamedQuery(name = "ReporteMatricula.findByParalelo", query = "SELECT r FROM ReporteMatricula r WHERE r.paralelo = :paralelo"),
    @NamedQuery(name = "ReporteMatricula.findByEsAprobado", query = "SELECT r FROM ReporteMatricula r WHERE r.esAprobado = :esAprobado"),
    @NamedQuery(name = "ReporteMatricula.findByNota", query = "SELECT r FROM ReporteMatricula r WHERE r.nota = :nota")})
public class ReporteMatricula implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "matricula_id")
    private Long matriculaId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "modulo_matriculado")
    private String moduloMatriculado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "numero_modulo_matriculado")
    private String numeroModuloMatriculado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "paralelo")
    private String paralelo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_aprobado")
    private Boolean esAprobado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nota")
    private Double nota;
    @JoinColumn(name = "oferta_academica_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private OfertaAcademica ofertaAcademicaId;
    @JoinColumn(name = "estudiante_carrera_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EstudianteCarrera estudianteCarreraId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado_matricula_id")
    private Long estadoMatriculaId;
    @Transient
    private String estado;

    public ReporteMatricula() {
    }

    public ReporteMatricula(Boolean esAprobado) {
        this.esAprobado = esAprobado;
    }

    public ReporteMatricula(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMatriculaId() {
        return matriculaId;
    }

    public void setMatriculaId(Long matriculaId) {
        this.matriculaId = matriculaId;
    }

    public String getModuloMatriculado() {
        return moduloMatriculado;
    }

    public void setModuloMatriculado(String moduloMatriculado) {
        this.moduloMatriculado = moduloMatriculado;
    }

    public String getNumeroModuloMatriculado() {
        return numeroModuloMatriculado;
    }

    public void setNumeroModuloMatriculado(String numeroModuloMatriculado) {
        this.numeroModuloMatriculado = numeroModuloMatriculado;
    }

    public String getParalelo() {
        return paralelo;
    }

    public void setParalelo(String paralelo) {
        this.paralelo = paralelo;
    }

    public Boolean isEsAprobado() {
        return esAprobado;
    }

    public void setEsAprobado(Boolean esAprobado) {
        this.esAprobado = esAprobado;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public Long getEstadoMatriculaId() {
        return estadoMatriculaId;
    }

    public void setEstadoMatriculaId(Long estadoMatriculaId) {
        this.estadoMatriculaId = estadoMatriculaId;
    }

    public OfertaAcademica getOfertaAcademicaId() {
        return ofertaAcademicaId;
    }

    public void setOfertaAcademicaId(OfertaAcademica ofertaAcademicaId) {
        this.ofertaAcademicaId = ofertaAcademicaId;
    }

    public EstudianteCarrera getEstudianteCarreraId() {
        return estudianteCarreraId;
    }

    public void setEstudianteCarreraId(EstudianteCarrera estudianteCarreraId) {
        this.estudianteCarreraId = estudianteCarreraId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
        if (!(object instanceof ReporteMatricula)) {
            return false;
        }
        ReporteMatricula other = (ReporteMatricula) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.jlmallas.academico.entity.ReporteMatricula[ id=" + id + " ]";
    }

}
