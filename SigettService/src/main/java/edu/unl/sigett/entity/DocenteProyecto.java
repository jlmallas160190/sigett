/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "docente_proyecto")
@XmlRootElement
@Cacheable(value = false)
public class DocenteProyecto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "docente_carrera_id")
    private Long docenteCarreraId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_activo")
    private Boolean esActivo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "docenteProyectoId")
    private List<Pertinencia> pertinenciaList;
    @JoinColumn(name = "proyecto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Proyecto proyectoId;
    @Transient
    private Long estadoProyecto;

    public DocenteProyecto() {
        this.pertinenciaList = new ArrayList<>();
    }

    public DocenteProyecto(Long id) {
        this.id = id;
    }

    public DocenteProyecto(Proyecto proyecto, Date fecha, Long docenteCarreraId, Boolean esActivo, Long estadoProyecto) {
        this.proyectoId = proyecto;
        this.fecha = fecha;
        this.docenteCarreraId = docenteCarreraId;
        this.esActivo = esActivo;
        this.estadoProyecto = estadoProyecto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Long getDocenteCarreraId() {
        return docenteCarreraId;
    }

    public void setDocenteCarreraId(Long docenteCarreraId) {
        this.docenteCarreraId = docenteCarreraId;
    }

    public Boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(Boolean esActivo) {
        this.esActivo = esActivo;
    }

    @XmlTransient
    public List<Pertinencia> getPertinenciaList() {
        return pertinenciaList;
    }

    public void setPertinenciaList(List<Pertinencia> pertinenciaList) {
        this.pertinenciaList = pertinenciaList;
    }

    public Proyecto getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Proyecto proyectoId) {
        this.proyectoId = proyectoId;
    }

    public Long getEstadoProyecto() {
        return estadoProyecto;
    }

    public void setEstadoProyecto(Long estadoProyecto) {
        this.estadoProyecto = estadoProyecto;
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
        if (!(object instanceof DocenteProyecto)) {
            return false;
        }
        DocenteProyecto other = (DocenteProyecto) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.DocenteProyecto[ id=" + id + " ]";
    }

}
