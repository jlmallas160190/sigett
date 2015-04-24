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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "calificacion_parametro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CalificacionParametro.findAll", query = "SELECT c FROM CalificacionParametro c"),
    @NamedQuery(name = "CalificacionParametro.findById", query = "SELECT c FROM CalificacionParametro c WHERE c.id = :id"),
    @NamedQuery(name = "CalificacionParametro.findByNota", query = "SELECT c FROM CalificacionParametro c WHERE c.nota = :nota")})
public class CalificacionParametro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nota")
    private double nota;
    @JoinColumn(name = "parametro_cat_ev_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ParametroCatalogoEvaluacion parametroCatEvId;
    @JoinColumn(name = "calificacion_miembro_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CalificacionMiembro calificacionMiembroId;

    public CalificacionParametro() {
    }

    public CalificacionParametro(Long id) {
        this.id = id;
    }

    public CalificacionParametro(Long id, double nota) {
        this.id = id;
        this.nota = nota;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public ParametroCatalogoEvaluacion getParametroCatEvId() {
        return parametroCatEvId;
    }

    public void setParametroCatEvId(ParametroCatalogoEvaluacion parametroCatEvId) {
        this.parametroCatEvId = parametroCatEvId;
    }

    public CalificacionMiembro getCalificacionMiembroId() {
        return calificacionMiembroId;
    }

    public void setCalificacionMiembroId(CalificacionMiembro calificacionMiembroId) {
        this.calificacionMiembroId = calificacionMiembroId;
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
        if (!(object instanceof CalificacionParametro)) {
            return false;
        }
        CalificacionParametro other = (CalificacionParametro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.CalificacionParametro[ id=" + id + " ]";
    }
    
}
