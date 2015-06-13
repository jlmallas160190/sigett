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
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author JorgeLuis
 */
@Entity
@Table(name = "estudiante", schema = "academico")
@Cacheable(value = false)
@XmlRootElement
public class Estudiante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estudianteId")
    private List<EstudianteCarrera> estudianteCarreraList;
    @Basic(optional = false)
    @Column(name = "es_activo")
    @NotNull
    private Boolean esActivo;

    public Estudiante() {
        this.estudianteCarreraList = new ArrayList<>();
    }

    public Estudiante(Boolean esActivo) {
        this.esActivo = esActivo;
    }

    public Estudiante(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlTransient
    public List<EstudianteCarrera> getEstudianteCarreraList() {
        return estudianteCarreraList;
    }

    public void setEstudianteCarreraList(List<EstudianteCarrera> estudianteCarreraList) {
        this.estudianteCarreraList = estudianteCarreraList;
    }

    public Boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(Boolean esActivo) {
        this.esActivo = esActivo;
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
        if (!(object instanceof Estudiante)) {
            return false;
        }
        Estudiante other = (Estudiante) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return id + "";
    }

}
