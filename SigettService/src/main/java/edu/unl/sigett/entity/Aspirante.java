/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
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
 * @author jorge-luis
 */
@Entity
@Table(name = "aspirante")
@Cacheable(value = false)
@XmlRootElement
public class Aspirante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_apto")
    private Boolean esApto;
    @OneToMany(mappedBy = "aspiranteId")
    private List<AutorProyecto> autorProyectoList;

    public Aspirante() {
    }

    public Aspirante(Long id) {
        this.id = id;
    }

    public Aspirante(Long id, Boolean esApto) {
        this.id = id;
        this.esApto = esApto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEsApto() {
        return esApto;
    }

    public void setEsApto(Boolean esApto) {
        this.esApto = esApto;
    }

    @XmlTransient
    public List<AutorProyecto> getAutorProyectoList() {
        return autorProyectoList;
    }

    public void setAutorProyectoList(List<AutorProyecto> autorProyectoList) {
        this.autorProyectoList = autorProyectoList;
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
        if (!(object instanceof Aspirante)) {
            return false;
        }
        Aspirante other = (Aspirante) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.Aspirante[ id=" + id + " ]";
    }

}
