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
@Table(name = "titulo", schema = "academico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Titulo.findAll", query = "SELECT t FROM Titulo t"),
    @NamedQuery(name = "Titulo.findById", query = "SELECT t FROM Titulo t WHERE t.id = :id"),
    @NamedQuery(name = "Titulo.findByNombre", query = "SELECT t FROM Titulo t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "Titulo.findByEsActivo", query = "SELECT t FROM Titulo t WHERE t.esActivo = :esActivo"),
    @NamedQuery(name = "Titulo.findByAbreviacion", query = "SELECT t FROM Titulo t WHERE t.abreviacion = :abreviacion")})
public class Titulo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_activo")
    private boolean esActivo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "abreviacion")
    private String abreviacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tituloId")
    private List<TituloDocente> tituloDocenteList;

    public Titulo() {
    }

    public Titulo(Integer id) {
        this.id = id;
    }

    public Titulo(Integer id, String nombre, boolean esActivo, String abreviacion) {
        this.id = id;
        this.nombre = nombre;
        this.esActivo = esActivo;
        this.abreviacion = abreviacion;
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

    public boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(boolean esActivo) {
        this.esActivo = esActivo;
    }

    public String getAbreviacion() {
        return abreviacion;
    }

    public void setAbreviacion(String abreviacion) {
        this.abreviacion = abreviacion;
    }

    @XmlTransient
    public List<TituloDocente> getTituloDocenteList() {
        return tituloDocenteList;
    }

    public void setTituloDocenteList(List<TituloDocente> tituloDocenteList) {
        this.tituloDocenteList = tituloDocenteList;
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
        if (!(object instanceof Titulo)) {
            return false;
        }
        Titulo other = (Titulo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.jlmallas.academico.entity.Titulo[ id=" + id + " ]";
    }
    
}
