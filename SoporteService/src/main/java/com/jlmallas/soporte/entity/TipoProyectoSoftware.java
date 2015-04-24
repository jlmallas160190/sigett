/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jlmallas.soporte.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "tipo_proyecto_software",schema = "soporte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoProyectoSoftware.findAll", query = "SELECT t FROM TipoProyectoSoftware t"),
    @NamedQuery(name = "TipoProyectoSoftware.findById", query = "SELECT t FROM TipoProyectoSoftware t WHERE t.id = :id"),
    @NamedQuery(name = "TipoProyectoSoftware.findByNombre", query = "SELECT t FROM TipoProyectoSoftware t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "TipoProyectoSoftware.findByDescripcion", query = "SELECT t FROM TipoProyectoSoftware t WHERE t.descripcion = :descripcion")})
public class TipoProyectoSoftware implements Serializable {
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
    @Column(name = "descripcion")
    private String descripcion;

    public TipoProyectoSoftware() {
    }

    public TipoProyectoSoftware(Integer id) {
        this.id = id;
    }

    public TipoProyectoSoftware(Integer id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoProyectoSoftware)) {
            return false;
        }
        TipoProyectoSoftware other = (TipoProyectoSoftware) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jlmallas.soporte.entity.TipoProyectoSoftware[ id=" + id + " ]";
    }
    
}
