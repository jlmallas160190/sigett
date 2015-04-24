/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.entity;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "duracion_actividad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DuracionActividad.findAll", query = "SELECT d FROM DuracionActividad d"),
    @NamedQuery(name = "DuracionActividad.findById", query = "SELECT d FROM DuracionActividad d WHERE d.id = :id"),
    @NamedQuery(name = "DuracionActividad.findByDuracion", query = "SELECT d FROM DuracionActividad d WHERE d.duracion = :duracion")})
public class DuracionActividad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "duracion")
    private double duracion;
    @JoinColumn(name = "catalogo_duracion_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CatalogoDuracion catalogoDuracionId;

    public DuracionActividad() {
    }

    public DuracionActividad(Long id) {
        this.id = id;
    }

    public DuracionActividad(Long id, double duracion) {
        this.id = id;
        this.duracion = duracion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getDuracion() {
        return duracion;
    }

    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }

    public CatalogoDuracion getCatalogoDuracionId() {
        return catalogoDuracionId;
    }

    public void setCatalogoDuracionId(CatalogoDuracion catalogoDuracionId) {
        this.catalogoDuracionId = catalogoDuracionId;
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
        if (!(object instanceof DuracionActividad)) {
            return false;
        }
        DuracionActividad other = (DuracionActividad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.DuracionActividad[ id=" + id + " ]";
    }
    
}
