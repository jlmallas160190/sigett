/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "cronograma")
@XmlRootElement
@Cacheable(value = false)
public class Cronograma implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "duracion")
    private double duracion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "avance")
    private double avance;
    @Basic(optional = false)
    @NotNull
    @Column(name = "faltante")
    private double faltante;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_prorroga")
    @Temporal(TemporalType.DATE)
    private Date fechaProrroga;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cronogramaId")
    private List<Actividad> actividadList;
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Proyecto proyecto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cronogramaId")
    private List<DuracionCronograma> duracionCronogramaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cronogramaId")
    private List<Prorroga> prorrogaList;

    public Cronograma() {
    }

    public Cronograma(Long id) {
        this.id = id;
    }

    public Cronograma(Long id, Date fechaInicio, Date fechaFin, double duracion, double avance, double faltante, Date fechaProrroga) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.duracion = duracion;
        this.avance = avance;
        this.faltante = faltante;
        this.fechaProrroga = fechaProrroga;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public double getDuracion() {
        return duracion;
    }

    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }

    public double getAvance() {
        return avance;
    }

    public void setAvance(double avance) {
        this.avance = avance;
    }

    public double getFaltante() {
        return faltante;
    }

    public void setFaltante(double faltante) {
        this.faltante = faltante;
    }

    public Date getFechaProrroga() {
        return fechaProrroga;
    }

    public void setFechaProrroga(Date fechaProrroga) {
        this.fechaProrroga = fechaProrroga;
    }

    @XmlTransient
    public List<Actividad> getActividadList() {
        return actividadList;
    }

    public void setActividadList(List<Actividad> actividadList) {
        this.actividadList = actividadList;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    @XmlTransient
    public List<DuracionCronograma> getDuracionCronogramaList() {
        return duracionCronogramaList;
    }

    public void setDuracionCronogramaList(List<DuracionCronograma> duracionCronogramaList) {
        this.duracionCronogramaList = duracionCronogramaList;
    }

    @XmlTransient
    public List<Prorroga> getProrrogaList() {
        return prorrogaList;
    }

    public void setProrrogaList(List<Prorroga> prorrogaList) {
        this.prorrogaList = prorrogaList;
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
        if (!(object instanceof Cronograma)) {
            return false;
        }
        Cronograma other = (Cronograma) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.Cronograma[ id=" + id + " ]";
    }
    
}
