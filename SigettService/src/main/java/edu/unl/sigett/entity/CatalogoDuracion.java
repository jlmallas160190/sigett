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
@Table(name = "catalogo_duracion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatalogoDuracion.findAll", query = "SELECT c FROM CatalogoDuracion c"),
    @NamedQuery(name = "CatalogoDuracion.findById", query = "SELECT c FROM CatalogoDuracion c WHERE c.id = :id"),
    @NamedQuery(name = "CatalogoDuracion.findByNombre", query = "SELECT c FROM CatalogoDuracion c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "CatalogoDuracion.findByDescripcion", query = "SELECT c FROM CatalogoDuracion c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "CatalogoDuracion.findByEsActivo", query = "SELECT c FROM CatalogoDuracion c WHERE c.esActivo = :esActivo")})
public class CatalogoDuracion implements Serializable {
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
    @Size(min = 1, max = 250)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_activo")
    private boolean esActivo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "catalogoDuracionId")
    private List<DuracionCronograma> duracionCronogramaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "catalogoDuracionId")
    private List<DuracionActividad> duracionActividadList;

    public CatalogoDuracion() {
    }

    public CatalogoDuracion(Integer id) {
        this.id = id;
    }

    public CatalogoDuracion(Integer id, String nombre, String descripcion, boolean esActivo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.esActivo = esActivo;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(boolean esActivo) {
        this.esActivo = esActivo;
    }

    @XmlTransient
    public List<DuracionCronograma> getDuracionCronogramaList() {
        return duracionCronogramaList;
    }

    public void setDuracionCronogramaList(List<DuracionCronograma> duracionCronogramaList) {
        this.duracionCronogramaList = duracionCronogramaList;
    }

    @XmlTransient
    public List<DuracionActividad> getDuracionActividadList() {
        return duracionActividadList;
    }

    public void setDuracionActividadList(List<DuracionActividad> duracionActividadList) {
        this.duracionActividadList = duracionActividadList;
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
        if (!(object instanceof CatalogoDuracion)) {
            return false;
        }
        CatalogoDuracion other = (CatalogoDuracion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id + ": " + nombre;
    }
    
}
