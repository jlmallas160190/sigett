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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author JorgeLuis
 */
@Entity
@Table(name = "docente_carrera", schema = "academico")
@Cacheable(value = false)
@XmlRootElement
public class DocenteCarrera implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "docente_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Docente docenteId;
    @JoinColumn(name = "carrera_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Carrera carreraId;
    @JoinColumn(name = "oferta_academica_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private OfertaAcademica ofertaAcademica;
    @Basic(optional = false)
    @Column(name = "es_activo")
    @NotNull
    private Boolean esActivo;

    public DocenteCarrera() {
    }

    public DocenteCarrera(Long id, Docente docente, Carrera carrera, OfertaAcademica ofertaAcademica, Boolean esActivo) {
        this.id = id;
        this.docenteId = docente;
        this.carreraId = carrera;
        this.esActivo = esActivo;
        this.ofertaAcademica = ofertaAcademica;
    }

    public DocenteCarrera(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Docente getDocenteId() {
        return docenteId;
    }

    public void setDocenteId(Docente docenteId) {
        this.docenteId = docenteId;
    }

    public Carrera getCarreraId() {
        return carreraId;
    }

    public void setCarreraId(Carrera carreraId) {
        this.carreraId = carreraId;
    }

    public Boolean isEsActivo() {
        return esActivo;
    }

    public void setEsActivo(Boolean esActivo) {
        this.esActivo = esActivo;
    }

    public OfertaAcademica getOfertaAcademica() {
        return ofertaAcademica;
    }

    public void setOfertaAcademica(OfertaAcademica ofertaAcademica) {
        this.ofertaAcademica = ofertaAcademica;
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
        if (!(object instanceof DocenteCarrera)) {
            return false;
        }
        DocenteCarrera other = (DocenteCarrera) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return id + "";
    }

}
