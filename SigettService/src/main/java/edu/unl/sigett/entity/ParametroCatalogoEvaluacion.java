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
@Table(name = "parametro_catalogo_evaluacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParametroCatalogoEvaluacion.findAll", query = "SELECT p FROM ParametroCatalogoEvaluacion p"),
    @NamedQuery(name = "ParametroCatalogoEvaluacion.findById", query = "SELECT p FROM ParametroCatalogoEvaluacion p WHERE p.id = :id")})
public class ParametroCatalogoEvaluacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parametroCatEvId")
    private List<CalificacionParametro> calificacionParametroList;
    @JoinColumn(name = "parametro_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Parametro parametroId;
    @JoinColumn(name = "catalogo_evaluacion_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CatalogoEvaluacion catalogoEvaluacionId;

    public ParametroCatalogoEvaluacion() {
    }

    public ParametroCatalogoEvaluacion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlTransient
    public List<CalificacionParametro> getCalificacionParametroList() {
        return calificacionParametroList;
    }

    public void setCalificacionParametroList(List<CalificacionParametro> calificacionParametroList) {
        this.calificacionParametroList = calificacionParametroList;
    }

    public Parametro getParametroId() {
        return parametroId;
    }

    public void setParametroId(Parametro parametroId) {
        this.parametroId = parametroId;
    }

    public CatalogoEvaluacion getCatalogoEvaluacionId() {
        return catalogoEvaluacionId;
    }

    public void setCatalogoEvaluacionId(CatalogoEvaluacion catalogoEvaluacionId) {
        this.catalogoEvaluacionId = catalogoEvaluacionId;
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
        if (!(object instanceof ParametroCatalogoEvaluacion)) {
            return false;
        }
        ParametroCatalogoEvaluacion other = (ParametroCatalogoEvaluacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.ParametroCatalogoEvaluacion[ id=" + id + " ]";
    }
    
}
