/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "plazo_evaluacion_tribunal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlazoEvaluacionTribunal.findAll", query = "SELECT p FROM PlazoEvaluacionTribunal p"),
    @NamedQuery(name = "PlazoEvaluacionTribunal.findById", query = "SELECT p FROM PlazoEvaluacionTribunal p WHERE p.id = :id"),
    @NamedQuery(name = "PlazoEvaluacionTribunal.findByFechaPlazo", query = "SELECT p FROM PlazoEvaluacionTribunal p WHERE p.fechaPlazo = :fechaPlazo"),
    @NamedQuery(name = "PlazoEvaluacionTribunal.findByFechaInicial", query = "SELECT p FROM PlazoEvaluacionTribunal p WHERE p.fechaInicial = :fechaInicial")})
public class PlazoEvaluacionTribunal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_plazo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPlazo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicial")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicial;
    @JoinColumn(name = "evaluacion_tribunal_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EvaluacionTribunal evaluacionTribunalId;

    public PlazoEvaluacionTribunal() {
    }

    public PlazoEvaluacionTribunal(Long id) {
        this.id = id;
    }

    public PlazoEvaluacionTribunal(Long id, Date fechaPlazo, Date fechaInicial) {
        this.id = id;
        this.fechaPlazo = fechaPlazo;
        this.fechaInicial = fechaInicial;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaPlazo() {
        return fechaPlazo;
    }

    public void setFechaPlazo(Date fechaPlazo) {
        this.fechaPlazo = fechaPlazo;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public EvaluacionTribunal getEvaluacionTribunalId() {
        return evaluacionTribunalId;
    }

    public void setEvaluacionTribunalId(EvaluacionTribunal evaluacionTribunalId) {
        this.evaluacionTribunalId = evaluacionTribunalId;
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
        if (!(object instanceof PlazoEvaluacionTribunal)) {
            return false;
        }
        PlazoEvaluacionTribunal other = (PlazoEvaluacionTribunal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.PlazoEvaluacionTribunal[ id=" + id + " ]";
    }
    
}
