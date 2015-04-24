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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "rango_equivalencia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RangoEquivalencia.findAll", query = "SELECT r FROM RangoEquivalencia r"),
    @NamedQuery(name = "RangoEquivalencia.findById", query = "SELECT r FROM RangoEquivalencia r WHERE r.id = :id"),
    @NamedQuery(name = "RangoEquivalencia.findByNotaInicio", query = "SELECT r FROM RangoEquivalencia r WHERE r.notaInicio = :notaInicio"),
    @NamedQuery(name = "RangoEquivalencia.findByNotaFin", query = "SELECT r FROM RangoEquivalencia r WHERE r.notaFin = :notaFin")})
public class RangoEquivalencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nota_inicio")
    private double notaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nota_fin")
    private double notaFin;
    @JoinColumn(name = "rango_nota_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RangoNota rangoNotaId;
    @JoinColumn(name = "equivalencia_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Equivalencia equivalenciaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rangoEquivalenciaId")
    private List<EvaluacionTribunal> evaluacionTribunalList;

    public RangoEquivalencia() {
    }

    public RangoEquivalencia(Long id) {
        this.id = id;
    }

    public RangoEquivalencia(Long id, double notaInicio, double notaFin) {
        this.id = id;
        this.notaInicio = notaInicio;
        this.notaFin = notaFin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getNotaInicio() {
        return notaInicio;
    }

    public void setNotaInicio(double notaInicio) {
        this.notaInicio = notaInicio;
    }

    public double getNotaFin() {
        return notaFin;
    }

    public void setNotaFin(double notaFin) {
        this.notaFin = notaFin;
    }

    public RangoNota getRangoNotaId() {
        return rangoNotaId;
    }

    public void setRangoNotaId(RangoNota rangoNotaId) {
        this.rangoNotaId = rangoNotaId;
    }

    public Equivalencia getEquivalenciaId() {
        return equivalenciaId;
    }

    public void setEquivalenciaId(Equivalencia equivalenciaId) {
        this.equivalenciaId = equivalenciaId;
    }

    @XmlTransient
    public List<EvaluacionTribunal> getEvaluacionTribunalList() {
        return evaluacionTribunalList;
    }

    public void setEvaluacionTribunalList(List<EvaluacionTribunal> evaluacionTribunalList) {
        this.evaluacionTribunalList = evaluacionTribunalList;
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
        if (!(object instanceof RangoEquivalencia)) {
            return false;
        }
        RangoEquivalencia other = (RangoEquivalencia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.RangoEquivalencia[ id=" + id + " ]";
    }
    
}
