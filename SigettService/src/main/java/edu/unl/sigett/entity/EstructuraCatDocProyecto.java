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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "estructura_cat_doc_proyecto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstructuraCatDocProyecto.findAll", query = "SELECT e FROM EstructuraCatDocProyecto e"),
    @NamedQuery(name = "EstructuraCatDocProyecto.findById", query = "SELECT e FROM EstructuraCatDocProyecto e WHERE e.id = :id")})
public class EstructuraCatDocProyecto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estructuraCatalgoId")
    private List<EstructuraDocumentoProyecto> estructuraDocumentoProyectoList;
    @JoinColumn(name = "estructura_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Estructura estructuraId;
    @JoinColumn(name = "catalogo_documento_proyecto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CatalogoDocumentoProyecto catalogoDocumentoProyectoId;

    public EstructuraCatDocProyecto() {
    }

    public EstructuraCatDocProyecto(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlTransient
    public List<EstructuraDocumentoProyecto> getEstructuraDocumentoProyectoList() {
        return estructuraDocumentoProyectoList;
    }

    public void setEstructuraDocumentoProyectoList(List<EstructuraDocumentoProyecto> estructuraDocumentoProyectoList) {
        this.estructuraDocumentoProyectoList = estructuraDocumentoProyectoList;
    }

    public Estructura getEstructuraId() {
        return estructuraId;
    }

    public void setEstructuraId(Estructura estructuraId) {
        this.estructuraId = estructuraId;
    }

    public CatalogoDocumentoProyecto getCatalogoDocumentoProyectoId() {
        return catalogoDocumentoProyectoId;
    }

    public void setCatalogoDocumentoProyectoId(CatalogoDocumentoProyecto catalogoDocumentoProyectoId) {
        this.catalogoDocumentoProyectoId = catalogoDocumentoProyectoId;
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
        if (!(object instanceof EstructuraCatDocProyecto)) {
            return false;
        }
        EstructuraCatDocProyecto other = (EstructuraCatDocProyecto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.EstructuraCatDocProyecto[ id=" + id + " ]";
    }
    
}
