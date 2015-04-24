/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
@Table(name = "aspirante")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Aspirante.findAll", query = "SELECT a FROM Aspirante a"),
    @NamedQuery(name = "Aspirante.findById", query = "SELECT a FROM Aspirante a WHERE a.id = :id"),
    @NamedQuery(name = "Aspirante.findByEsApto", query = "SELECT a FROM Aspirante a WHERE a.esApto = :esApto")})
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
    private boolean esApto;
    @OneToMany(mappedBy = "aspiranteId")
    private List<AutorProyecto> autorProyectoList;

    public Aspirante() {
    }

    public Aspirante(Long id) {
        this.id = id;
    }

    public Aspirante(Long id, boolean esApto) {
        this.id = id;
        this.esApto = esApto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getEsApto() {
        return esApto;
    }

    public void setEsApto(boolean esApto) {
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
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.Aspirante[ id=" + id + " ]";
    }
    
}
