/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "director")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Director.findAll", query = "SELECT d FROM Director d"),
    @NamedQuery(name = "Director.findById", query = "SELECT d FROM Director d WHERE d.id = :id"),
    @NamedQuery(name = "Director.findByEsActivo", query = "SELECT d FROM Director d WHERE d.esActivo = :esActivo")})
public class Director implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_activo")
    private Boolean esActivo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "directorId")
    private List<DirectorProyecto> directorProyectoList;

    public Director() {
    }

    public Director(Long id) {
        this.id = id;
    }

    public Director(Long id, Boolean esActivo) {
        this.id = id;
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

    @XmlTransient
    public List<DirectorProyecto> getDirectorProyectoList() {
        return directorProyectoList;
    }

    public void setDirectorProyectoList(List<DirectorProyecto> directorProyectoList) {
        this.directorProyectoList = directorProyectoList;
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
        if (!(object instanceof Director)) {
            return false;
        }
        Director other = (Director) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.Director[ id=" + id + " ]";
    }

}
