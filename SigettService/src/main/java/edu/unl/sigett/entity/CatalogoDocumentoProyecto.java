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
@Table(name = "catalogo_documento_proyecto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatalogoDocumentoProyecto.findAll", query = "SELECT c FROM CatalogoDocumentoProyecto c"),
    @NamedQuery(name = "CatalogoDocumentoProyecto.findById", query = "SELECT c FROM CatalogoDocumentoProyecto c WHERE c.id = :id"),
    @NamedQuery(name = "CatalogoDocumentoProyecto.findByNombre", query = "SELECT c FROM CatalogoDocumentoProyecto c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "CatalogoDocumentoProyecto.findByDescripcion", query = "SELECT c FROM CatalogoDocumentoProyecto c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "CatalogoDocumentoProyecto.findByEsActivo", query = "SELECT c FROM CatalogoDocumentoProyecto c WHERE c.esActivo = :esActivo")})
public class CatalogoDocumentoProyecto implements Serializable {
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
    @Column(name = "es_activo")
    private Boolean esActivo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "catalogoDocumentoProyectoId")
    private List<DocumentoProyecto> documentoProyectoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "catalogoDocumentoProyectoId")
    private List<EstructuraCatDocProyecto> estructuraCatDocProyectoList;

    public CatalogoDocumentoProyecto() {
    }

    public CatalogoDocumentoProyecto(Integer id) {
        this.id = id;
    }

    public CatalogoDocumentoProyecto(Integer id, String nombre, String descripcion) {
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

    public Boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(Boolean esActivo) {
        this.esActivo = esActivo;
    }

    @XmlTransient
    public List<DocumentoProyecto> getDocumentoProyectoList() {
        return documentoProyectoList;
    }

    public void setDocumentoProyectoList(List<DocumentoProyecto> documentoProyectoList) {
        this.documentoProyectoList = documentoProyectoList;
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
        if (!(object instanceof CatalogoDocumentoProyecto)) {
            return false;
        }
        CatalogoDocumentoProyecto other = (CatalogoDocumentoProyecto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.CatalogoDocumentoProyecto[ id=" + id + " ]";
    }
    
}
