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
@Table(name = "estado_autor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoAutor.findAll", query = "SELECT e FROM EstadoAutor e"),
    @NamedQuery(name = "EstadoAutor.findById", query = "SELECT e FROM EstadoAutor e WHERE e.id = :id"),
    @NamedQuery(name = "EstadoAutor.findByNombre", query = "SELECT e FROM EstadoAutor e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "EstadoAutor.findByDescripcion", query = "SELECT e FROM EstadoAutor e WHERE e.descripcion = :descripcion"),
    @NamedQuery(name = "EstadoAutor.findByCodigo", query = "SELECT e FROM EstadoAutor e WHERE e.codigo = :codigo")})
public class EstadoAutor implements Serializable {
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
    @Size(max = 25)
    @Column(name = "codigo")
    private String codigo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoAutorId")
    private List<AutorProyecto> autorProyectoList;

    public EstadoAutor() {
    }

    public EstadoAutor(Integer id) {
        this.id = id;
    }

    public EstadoAutor(Integer id, String nombre, String descripcion) {
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
        if (!(object instanceof EstadoAutor)) {
            return false;
        }
        EstadoAutor other = (EstadoAutor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.EstadoAutor[ id=" + id + " ]";
    }
    
}
