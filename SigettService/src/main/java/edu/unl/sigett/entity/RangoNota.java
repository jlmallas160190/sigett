/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "rango_nota")
@XmlRootElement
public class RangoNota implements Serializable {

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
    @Column(name = "valor_inicio")
    private BigDecimal valorInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor_fin")
    private BigDecimal valorFin;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rangoNotaId")
    private List<RangoEquivalencia> rangoEquivalenciaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rangoNotaId")
    private List<EvaluacionTribunal> evaluacionTribunalList;

    public RangoNota() {
    }

    public RangoNota(Integer id) {
        this.id = id;
    }

    public RangoNota(Integer id, String nombre, BigDecimal valorInicio, BigDecimal valorFin) {
        this.id = id;
        this.nombre = nombre;
        this.valorInicio = valorInicio;
        this.valorFin = valorFin;
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

    public BigDecimal getValorInicio() {
        return valorInicio;
    }

    public void setValorInicio(BigDecimal valorInicio) {
        this.valorInicio = valorInicio;
    }

    public BigDecimal getValorFin() {
        return valorFin;
    }

    public void setValorFin(BigDecimal valorFin) {
        this.valorFin = valorFin;
    }

    @XmlTransient
    public List<RangoEquivalencia> getRangoEquivalenciaList() {
        return rangoEquivalenciaList;
    }

    public void setRangoEquivalenciaList(List<RangoEquivalencia> rangoEquivalenciaList) {
        this.rangoEquivalenciaList = rangoEquivalenciaList;
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
        if (!(object instanceof RangoNota)) {
            return false;
        }
        RangoNota other = (RangoNota) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.RangoNota[ id=" + id + " ]";
    }

}
