/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jlmallas.soporte.entity;

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
@Table(name = "proyecto_software",schema = "soporte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProyectoSoftware.findAll", query = "SELECT p FROM ProyectoSoftware p"),
    @NamedQuery(name = "ProyectoSoftware.findById", query = "SELECT p FROM ProyectoSoftware p WHERE p.id = :id"),
    @NamedQuery(name = "ProyectoSoftware.findByNombre", query = "SELECT p FROM ProyectoSoftware p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "ProyectoSoftware.findByDescripcion", query = "SELECT p FROM ProyectoSoftware p WHERE p.descripcion = :descripcion")})
public class ProyectoSoftware implements Serializable {
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proyectoSoftwareId")
    private List<Objeto> objetoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proyectoSoftwareId")
    private List<EncargadoProyectoSoftware> encargadoProyectoSoftwareList;

    public ProyectoSoftware() {
    }

    public ProyectoSoftware(Integer id) {
        this.id = id;
    }

    public ProyectoSoftware(Integer id, String nombre, String descripcion) {
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

    @XmlTransient
    public List<Objeto> getObjetoList() {
        return objetoList;
    }

    public void setObjetoList(List<Objeto> objetoList) {
        this.objetoList = objetoList;
    }

    @XmlTransient
    public List<EncargadoProyectoSoftware> getEncargadoProyectoSoftwareList() {
        return encargadoProyectoSoftwareList;
    }

    public void setEncargadoProyectoSoftwareList(List<EncargadoProyectoSoftware> encargadoProyectoSoftwareList) {
        this.encargadoProyectoSoftwareList = encargadoProyectoSoftwareList;
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
        if (!(object instanceof ProyectoSoftware)) {
            return false;
        }
        ProyectoSoftware other = (ProyectoSoftware) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jlmallas.soporte.entity.ProyectoSoftware[ id=" + id + " ]";
    }
    
}
