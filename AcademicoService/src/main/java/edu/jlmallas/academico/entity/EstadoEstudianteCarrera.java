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
@Table(name = "estado_estudiante_carrera", schema = "academico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoEstudianteCarrera.findAll", query = "SELECT e FROM EstadoEstudianteCarrera e"),
    @NamedQuery(name = "EstadoEstudianteCarrera.findById", query = "SELECT e FROM EstadoEstudianteCarrera e WHERE e.id = :id"),
    @NamedQuery(name = "EstadoEstudianteCarrera.findByNombre", query = "SELECT e FROM EstadoEstudianteCarrera e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "EstadoEstudianteCarrera.findByCodigo", query = "SELECT e FROM EstadoEstudianteCarrera e WHERE e.codigo = :codigo")})
public class EstadoEstudianteCarrera implements Serializable {

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
    @Size(min = 1, max = 100)
    @Column(name = "codigo")
    private String codigo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoId")
    private List<EstudianteCarrera> estudianteCarreraList;

    public EstadoEstudianteCarrera() {
    }

    public EstadoEstudianteCarrera(Integer id) {
        this.id = id;
    }

    public EstadoEstudianteCarrera(Integer id, String nombre, String codigo) {
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @XmlTransient
    public List<EstudianteCarrera> getEstudianteCarreraList() {
        return estudianteCarreraList;
    }

    public void setEstudianteCarreraList(List<EstudianteCarrera> estudianteCarreraList) {
        this.estudianteCarreraList = estudianteCarreraList;
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
        if (!(object instanceof EstadoEstudianteCarrera)) {
            return false;
        }
        EstadoEstudianteCarrera other = (EstadoEstudianteCarrera) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.jlmallas.academico.entity.EstadoEstudianteCarrera[ id=" + id + " ]";
    }

}
