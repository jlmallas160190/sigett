/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.entity;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "estudiante_carrera",schema = "academico")
@XmlRootElement
public class EstudianteCarrera implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_activo")
    private Boolean esActivo;
    @JoinColumn(name = "estudiante_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Estudiante estudianteId;
    @Basic(optional = false)
    @Column(name = "estado_id")
    private Long estadoId;
    @JoinColumn(name = "carrera_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Carrera carreraId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estudianteCarreraId")
    private List<ReporteMatricula> reporteMatriculaList;
    @Transient
    private String estado;

    public EstudianteCarrera() {
        this.reporteMatriculaList = new ArrayList<>();
    }

    public EstudianteCarrera(Long id) {
        this.id = id;
    }

    public EstudianteCarrera(Carrera carrera, Estudiante estudiante, Boolean esActivo,Long estadoId) {
        this.carreraId = carrera;
        this.estadoId=estadoId;
        this.estudianteId = estudiante;
        this.esActivo = esActivo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(Boolean esActivo) {
        this.esActivo = esActivo;
    }

    public Estudiante getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(Estudiante estudianteId) {
        this.estudianteId = estudianteId;
    }

    public Long getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Long estadoId) {
        this.estadoId = estadoId;
    }

    public Carrera getCarreraId() {
        return carreraId;
    }

    public void setCarreraId(Carrera carreraId) {
        this.carreraId = carreraId;
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
        if (!(object instanceof EstudianteCarrera)) {
            return false;
        }
        EstudianteCarrera other = (EstudianteCarrera) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "edu.jlmallas.academico.entity.EstudianteCarrera[ id=" + id + " ]";
    }

}
