/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "estructura_documento_proyecto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstructuraDocumentoProyecto.findAll", query = "SELECT e FROM EstructuraDocumentoProyecto e"),
    @NamedQuery(name = "EstructuraDocumentoProyecto.findById", query = "SELECT e FROM EstructuraDocumentoProyecto e WHERE e.id = :id")})
public class EstructuraDocumentoProyecto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "estructura_catalgo_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EstructuraCatDocProyecto estructuraCatalgoId;
    @JoinColumn(name = "documento_proyecto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DocumentoProyecto documentoProyectoId;

    public EstructuraDocumentoProyecto() {
    }

    public EstructuraDocumentoProyecto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstructuraCatDocProyecto getEstructuraCatalgoId() {
        return estructuraCatalgoId;
    }

    public void setEstructuraCatalgoId(EstructuraCatDocProyecto estructuraCatalgoId) {
        this.estructuraCatalgoId = estructuraCatalgoId;
    }

    public DocumentoProyecto getDocumentoProyectoId() {
        return documentoProyectoId;
    }

    public void setDocumentoProyectoId(DocumentoProyecto documentoProyectoId) {
        this.documentoProyectoId = documentoProyectoId;
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
        if (!(object instanceof EstructuraDocumentoProyecto)) {
            return false;
        }
        EstructuraDocumentoProyecto other = (EstructuraDocumentoProyecto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.EstructuraDocumentoProyecto[ id=" + id + " ]";
    }
    
}
