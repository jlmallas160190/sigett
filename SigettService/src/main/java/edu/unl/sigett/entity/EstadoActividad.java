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
@Table(name = "estado_actividad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoActividad.findAll", query = "SELECT e FROM EstadoActividad e"),
    @NamedQuery(name = "EstadoActividad.findById", query = "SELECT e FROM EstadoActividad e WHERE e.id = :id"),
    @NamedQuery(name = "EstadoActividad.findByNombre", query = "SELECT e FROM EstadoActividad e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "EstadoActividad.findByObservacion", query = "SELECT e FROM EstadoActividad e WHERE e.observacion = :observacion"),
    @NamedQuery(name = "EstadoActividad.findByEsActivo", query = "SELECT e FROM EstadoActividad e WHERE e.esActivo = :esActivo")})
public class EstadoActividad implements Serializable {
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_activo")
    private boolean esActivo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoActividadId")
    private List<Actividad> actividadList;

    public EstadoActividad() {
    }

    public EstadoActividad(Integer id) {
        this.id = id;
    }

    public EstadoActividad(Integer id, String nombre, String observacion, boolean esActivo) {
        this.id = id;
        this.nombre = nombre;
        this.observacion = observacion;
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

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(boolean esActivo) {
        this.esActivo = esActivo;
    }

    @XmlTransient
    public List<Actividad> getActividadList() {
        return actividadList;
    }

    public void setActividadList(List<Actividad> actividadList) {
        this.actividadList = actividadList;
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
        if (!(object instanceof EstadoActividad)) {
            return false;
        }
        EstadoActividad other = (EstadoActividad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.EstadoActividad[ id=" + id + " ]";
    }
    
}
