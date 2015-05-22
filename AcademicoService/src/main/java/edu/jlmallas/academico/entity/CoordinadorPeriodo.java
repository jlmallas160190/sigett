/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
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
 * @author JorgeLuis
 */
@Entity
@Table(name = "coordinador_periodo", schema = "academico")
@Cacheable(value = false)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoordinadorPeriodo.findAll", query = "SELECT c FROM CoordinadorPeriodo c"),
    @NamedQuery(name = "CoordinadorPeriodo.findById", query = "SELECT c FROM CoordinadorPeriodo c WHERE c.id = :id"),
    @NamedQuery(name = "CoordinadorPeriodo.findByEsVigente", query = "SELECT c FROM CoordinadorPeriodo c WHERE c.esVigente = :esVigente")})
public class CoordinadorPeriodo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_vigente")
    private Boolean esVigente;
    @JoinColumn(name = "periodo_id", referencedColumnName = "id")
    @ManyToOne
    private PeriodoCoordinacion periodoId;
    @JoinColumn(name = "coordinador_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Coordinador coordinadorId;

    public CoordinadorPeriodo() {
    }

    public CoordinadorPeriodo(Boolean esVigente, Coordinador coordinador, PeriodoCoordinacion periodoCoordinacion) {
        this.esVigente = esVigente;
        this.periodoId = periodoCoordinacion;
        this.coordinadorId = coordinador;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEsVigente() {
        return esVigente;
    }

    public void setEsVigente(Boolean esVigente) {
        this.esVigente = esVigente;
    }

    public PeriodoCoordinacion getPeriodoId() {
        return periodoId;
    }

    public void setPeriodoId(PeriodoCoordinacion periodoId) {
        this.periodoId = periodoId;
    }

    public Coordinador getCoordinadorId() {
        return coordinadorId;
    }

    public void setCoordinadorId(Coordinador coordinadorId) {
        this.coordinadorId = coordinadorId;
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
        if (!(object instanceof CoordinadorPeriodo)) {
            return false;
        }
        CoordinadorPeriodo other = (CoordinadorPeriodo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CoordinadorPeriodo[ id=" + id + " ]";
    }

}
