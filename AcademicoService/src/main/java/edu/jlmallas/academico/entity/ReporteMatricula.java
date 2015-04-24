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
    private boolean esAprobado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nota")
    private double nota;
    @JoinColumn(name = "oferta_academica_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private OfertaAcademica ofertaAcademicaId;
    @JoinColumn(name = "estudiante_carrera_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EstudianteCarrera estudianteCarreraId;
    @JoinColumn(name = "estado_matricula_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EstadoMatricula estadoMatriculaId;

    public ReporteMatricula() {
    }

    public ReporteMatricula(Long id) {
        this.id = id;
    }

    public ReporteMatricula(Long id, String moduloMatriculado, String numeroModuloMatriculado, String paralelo, boolean esAprobado, double nota) {
        this.id = id;
        this.moduloMatriculado = moduloMatriculado;
        this.numeroModuloMatriculado = numeroModuloMatriculado;
        this.paralelo = paralelo;
        this.esAprobado = esAprobado;
        this.nota = nota;
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

    public boolean getEsAprobado() {
        return esAprobado;
    }

    public void setEsAprobado(boolean esAprobado) {
        this.esAprobado = esAprobado;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
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

    public EstadoMatricula getEstadoMatriculaId() {
        return estadoMatriculaId;
    }

    public void setEstadoMatriculaId(EstadoMatricula estadoMatriculaId) {
        this.estadoMatriculaId = estadoMatriculaId;
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
