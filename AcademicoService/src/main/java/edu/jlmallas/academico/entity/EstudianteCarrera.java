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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "estudiante_carrera")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstudianteCarrera.findAll", query = "SELECT e FROM EstudianteCarrera e"),
    @NamedQuery(name = "EstudianteCarrera.findById", query = "SELECT e FROM EstudianteCarrera e WHERE e.id = :id"),
    @NamedQuery(name = "EstudianteCarrera.findByEsActivo", query = "SELECT e FROM EstudianteCarrera e WHERE e.esActivo = :esActivo")})
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
    private boolean esActivo;
    @JoinColumn(name = "estudiante_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Estudiante estudianteId;
    @JoinColumn(name = "estado_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EstadoEstudianteCarrera estadoId;
    @JoinColumn(name = "carrera_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Carrera carreraId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estudianteCarreraId")
    private List<ReporteMatricula> reporteMatriculaList;

    public EstudianteCarrera() {
        this.reporteMatriculaList= new ArrayList<>();
    }

    public EstudianteCarrera(Long id) {
        this.id = id;
    }

    public EstudianteCarrera(Long id, boolean esActivo) {
        this.id = id;
        this.esActivo = esActivo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(boolean esActivo) {
        this.esActivo = esActivo;
    }

    public Estudiante getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(Estudiante estudianteId) {
        this.estudianteId = estudianteId;
    }

    public EstadoEstudianteCarrera getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(EstadoEstudianteCarrera estadoId) {
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

    @Override
    public String toString() {
        return "edu.jlmallas.academico.entity.EstudianteCarrera[ id=" + id + " ]";
    }
    
}
