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
@Table(name = "linea_investigacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LineaInvestigacion.findAll", query = "SELECT l FROM LineaInvestigacion l"),
    @NamedQuery(name = "LineaInvestigacion.findById", query = "SELECT l FROM LineaInvestigacion l WHERE l.id = :id"),
    @NamedQuery(name = "LineaInvestigacion.findByNombre", query = "SELECT l FROM LineaInvestigacion l WHERE l.nombre = :nombre"),
    @NamedQuery(name = "LineaInvestigacion.findByDescripcion", query = "SELECT l FROM LineaInvestigacion l WHERE l.descripcion = :descripcion"),
    @NamedQuery(name = "LineaInvestigacion.findByEsActivo", query = "SELECT l FROM LineaInvestigacion l WHERE l.esActivo = :esActivo")})
public class LineaInvestigacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
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
    @Column(name = "es_activo")
    private Boolean esActivo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lineaInvestigacionId")
    private List<LineaInvestigacionProyecto> lineaInvestigacionProyectoList;
    @OneToMany(mappedBy = "lineaInvestigacionId")
    private List<LineaInvestigacionDocente> lineaInvestigacionDocenteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lineaInvestigacionId")
    private List<LineaInvestigacionCarrera> lineaInvestigacionCarreraList;

    public LineaInvestigacion() {
        this.esActivo = true;
    }

    public LineaInvestigacion(Long id) {
        this.id = id;
    }

    public LineaInvestigacion(Long id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @XmlTransient
    public List<LineaInvestigacionProyecto> getLineaInvestigacionProyectoList() {
        return lineaInvestigacionProyectoList;
    }

    public void setLineaInvestigacionProyectoList(List<LineaInvestigacionProyecto> lineaInvestigacionProyectoList) {
        this.lineaInvestigacionProyectoList = lineaInvestigacionProyectoList;
    }

    @XmlTransient
    public List<LineaInvestigacionDocente> getLineaInvestigacionDocenteList() {
        return lineaInvestigacionDocenteList;
    }

    public void setLineaInvestigacionDocenteList(List<LineaInvestigacionDocente> lineaInvestigacionDocenteList) {
        this.lineaInvestigacionDocenteList = lineaInvestigacionDocenteList;
    }

    @XmlTransient
    public List<LineaInvestigacionCarrera> getLineaInvestigacionCarreraList() {
        return lineaInvestigacionCarreraList;
    }

    public void setLineaInvestigacionCarreraList(List<LineaInvestigacionCarrera> lineaInvestigacionCarreraList) {
        this.lineaInvestigacionCarreraList = lineaInvestigacionCarreraList;
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
        if (!(object instanceof LineaInvestigacion)) {
            return false;
        }
        LineaInvestigacion other = (LineaInvestigacion) object;
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
