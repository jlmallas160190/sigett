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
@Table(name = "autor_proyecto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AutorProyecto.findAll", query = "SELECT a FROM AutorProyecto a"),
    @NamedQuery(name = "AutorProyecto.findById", query = "SELECT a FROM AutorProyecto a WHERE a.id = :id"),
    @NamedQuery(name = "AutorProyecto.findByFechaInicio", query = "SELECT a FROM AutorProyecto a WHERE a.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "AutorProyecto.findByFechaCulminacion", query = "SELECT a FROM AutorProyecto a WHERE a.fechaCulminacion = :fechaCulminacion")})
public class AutorProyecto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_culminacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCulminacion;
    @JoinColumn(name = "proyecto_id", referencedColumnName = "id")
    @ManyToOne
    private Proyecto proyectoId;
    @Column(name = "estado_autor_id")
    private Long estadoAutorId;
    @JoinColumn(name = "aspirante_id", referencedColumnName = "id")
    @ManyToOne
    private Aspirante aspiranteId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "autorProyectoId")
    private List<Expediente> expedienteList;
    @Transient
    private Boolean esEditado;

    public AutorProyecto() {
        this.esEditado = false;
        this.expedienteList = new ArrayList<>();
    }

    public AutorProyecto(Long id) {
        this.id = id;
    }

    public AutorProyecto(Proyecto proyecto, Aspirante aspirante, Long estado, Date fechaInicio, Date fechaCulminacion) {
        this.proyectoId = proyecto;
        this.estadoAutorId = estado;
        this.aspiranteId = aspirante;
        this.fechaInicio = fechaInicio;
        this.fechaCulminacion = fechaCulminacion;
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

    public Date getFechaCulminacion() {
        return fechaCulminacion;
    }

    public void setFechaCulminacion(Date fechaCulminacion) {
        this.fechaCulminacion = fechaCulminacion;
    }

    public Proyecto getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Proyecto proyectoId) {
        this.proyectoId = proyectoId;
    }

    public Long getEstadoAutorId() {
        return estadoAutorId;
    }

    public void setEstadoAutorId(Long estadoAutorId) {
        this.estadoAutorId = estadoAutorId;
    }

    public Aspirante getAspiranteId() {
        return aspiranteId;
    }

    public void setAspiranteId(Aspirante aspiranteId) {
        this.aspiranteId = aspiranteId;
    }

    @XmlTransient
    public List<Expediente> getExpedienteList() {
        return expedienteList;
    }

    public void setExpedienteList(List<Expediente> expedienteList) {
        this.expedienteList = expedienteList;
    }

    public Boolean getEsEditado() {
        return esEditado;
    }

    public void setEsEditado(Boolean esEditado) {
        this.esEditado = esEditado;
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
        if (!(object instanceof AutorProyecto)) {
            return false;
        }
        AutorProyecto other = (AutorProyecto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.AutorProyecto[ id=" + id + " ]";
    }

}
