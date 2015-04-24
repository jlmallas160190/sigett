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
@Table(name = "estado_proyecto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoProyecto.findAll", query = "SELECT e FROM EstadoProyecto e"),
    @NamedQuery(name = "EstadoProyecto.findById", query = "SELECT e FROM EstadoProyecto e WHERE e.id = :id"),
    @NamedQuery(name = "EstadoProyecto.findByNombre", query = "SELECT e FROM EstadoProyecto e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "EstadoProyecto.findByDescripcion", query = "SELECT e FROM EstadoProyecto e WHERE e.descripcion = :descripcion"),
    @NamedQuery(name = "EstadoProyecto.findByEsActivo", query = "SELECT e FROM EstadoProyecto e WHERE e.esActivo = :esActivo"),
    @NamedQuery(name = "EstadoProyecto.findByCodigo", query = "SELECT e FROM EstadoProyecto e WHERE e.codigo = :codigo")})
public class EstadoProyecto implements Serializable {
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
    @Size(max = 500)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "es_activo")
    private Boolean esActivo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "codigo")
    private String codigo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoProyectoId")
    private List<Proyecto> proyectoList;

    public EstadoProyecto() {
    }

    public EstadoProyecto(Integer id) {
        this.id = id;
    }

    public EstadoProyecto(Integer id, String nombre, String codigo) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
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

    public Boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(Boolean esActivo) {
        this.esActivo = esActivo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @XmlTransient
    public List<Proyecto> getProyectoList() {
        return proyectoList;
    }

    public void setProyectoList(List<Proyecto> proyectoList) {
        this.proyectoList = proyectoList;
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
        if (!(object instanceof EstadoProyecto)) {
            return false;
        }
        EstadoProyecto other = (EstadoProyecto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.EstadoProyecto[ id=" + id + " ]";
    }
    
}
