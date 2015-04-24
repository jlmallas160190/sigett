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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "estructura")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estructura.findAll", query = "SELECT e FROM Estructura e"),
    @NamedQuery(name = "Estructura.findById", query = "SELECT e FROM Estructura e WHERE e.id = :id"),
    @NamedQuery(name = "Estructura.findByNombre", query = "SELECT e FROM Estructura e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "Estructura.findByObservacion", query = "SELECT e FROM Estructura e WHERE e.observacion = :observacion")})
public class Estructura implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "observacion")
    private String observacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estructuraId")
    private List<EstructuraCatDocProyecto> estructuraCatDocProyectoList;

    public Estructura() {
    }

    public Estructura(Integer id) {
        this.id = id;
    }

    public Estructura(Integer id, String nombre, String observacion) {
        this.id = id;
        this.nombre = nombre;
        this.observacion = observacion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    @XmlTransient
    public List<EstructuraCatDocProyecto> getEstructuraCatDocProyectoList() {
        return estructuraCatDocProyectoList;
    }

    public void setEstructuraCatDocProyectoList(List<EstructuraCatDocProyecto> estructuraCatDocProyectoList) {
        this.estructuraCatDocProyectoList = estructuraCatDocProyectoList;
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
        if (!(object instanceof Estructura)) {
            return false;
        }
        Estructura other = (Estructura) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.Estructura[ id=" + id + " ]";
    }
    
}
