/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.entity;

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
@Table(name = "coordinador", schema = "academico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Coordinador.findAll", query = "SELECT c FROM Coordinador c"),
    @NamedQuery(name = "Coordinador.findById", query = "SELECT c FROM Coordinador c WHERE c.id = :id"),
    @NamedQuery(name = "Coordinador.findByEsActivo", query = "SELECT c FROM Coordinador c WHERE c.esActivo = :esActivo")})
public class Coordinador implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_activo")
    private boolean esActivo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "coordinadorId")
    private List<CoordinadorPeriodo> coordinadorPeriodoList;

    public Coordinador() {
    }

    public Coordinador(Long id) {
        this.id = id;
    }

    public Coordinador(Long id, boolean esActivo) {
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

    @XmlTransient
    public List<CoordinadorPeriodo> getCoordinadorPeriodoList() {
        return coordinadorPeriodoList;
    }

    public void setCoordinadorPeriodoList(List<CoordinadorPeriodo> coordinadorPeriodoList) {
        this.coordinadorPeriodoList = coordinadorPeriodoList;
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
        if (!(object instanceof Coordinador)) {
            return false;
        }
        Coordinador other = (Coordinador) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.jlmallas.academico.entity.Coordinador[ id=" + id + " ]";
    }

}
