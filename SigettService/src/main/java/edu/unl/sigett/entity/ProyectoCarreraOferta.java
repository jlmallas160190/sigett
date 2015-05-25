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
@Table(name = "proyecto_carrera_oferta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProyectoCarreraOferta.findAll", query = "SELECT p FROM ProyectoCarreraOferta p"),
    @NamedQuery(name = "ProyectoCarreraOferta.findById", query = "SELECT p FROM ProyectoCarreraOferta p WHERE p.id = :id"),
    @NamedQuery(name = "ProyectoCarreraOferta.findByOfertaAcademicaId", query = "SELECT p FROM ProyectoCarreraOferta p WHERE p.ofertaAcademicaId = :ofertaAcademicaId"),
    @NamedQuery(name = "ProyectoCarreraOferta.findByCarreraId", query = "SELECT p FROM ProyectoCarreraOferta p WHERE p.carreraId = :carreraId"),
    @NamedQuery(name = "ProyectoCarreraOferta.findByEsActivo", query = "SELECT p FROM ProyectoCarreraOferta p WHERE p.esActivo = :esActivo")})
public class ProyectoCarreraOferta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "oferta_academica_id")
    private Long ofertaAcademicaId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "carrera_id")
    private Integer carreraId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_activo")
    private Boolean esActivo;
    @JoinColumn(name = "proyecto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Proyecto proyectoId;

    public ProyectoCarreraOferta() {
    }

    public ProyectoCarreraOferta(Long id) {
        this.id = id;
    }

    public ProyectoCarreraOferta(Proyecto proyecto, Integer carrreraId, Long ofertaId, Boolean esActivo) {
        this.proyectoId = proyecto;
        this.esActivo = esActivo;
        this.carreraId = carrreraId;
        this.ofertaAcademicaId = ofertaId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOfertaAcademicaId() {
        return ofertaAcademicaId;
    }

    public void setOfertaAcademicaId(Long ofertaAcademicaId) {
        this.ofertaAcademicaId = ofertaAcademicaId;
    }

    public Integer getCarreraId() {
        return carreraId;
    }

    public void setCarreraId(Integer carreraId) {
        this.carreraId = carreraId;
    }

    public Boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(Boolean esActivo) {
        this.esActivo = esActivo;
    }

    public Proyecto getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Proyecto proyectoId) {
        this.proyectoId = proyectoId;
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
        if (!(object instanceof ProyectoCarreraOferta)) {
            return false;
        }
        ProyectoCarreraOferta other = (ProyectoCarreraOferta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.ProyectoCarreraOferta[ id=" + id + " ]";
    }

}
