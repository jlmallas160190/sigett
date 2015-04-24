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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToMany;
import static javax.persistence.ParameterMode.IN;
import javax.persistence.StoredProcedureParameter;
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
@Table(name = "director_proyecto")
@Cacheable(value = false)
@XmlRootElement
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(
            name = "directorProyectoPorDocenteOferta",
            resultClasses = DirectorProyecto.class,
            procedureName = "sp_directores_proyecto_periodo",
            parameters = {
                @StoredProcedureParameter(mode = IN, name = "ci", type = String.class),
                @StoredProcedureParameter(mode = IN, name = "ofertaId", type = Long.class)
            }
    )})
@NamedQueries({
    @NamedQuery(name = "DirectorProyecto.findAll", query = "SELECT d FROM DirectorProyecto d"),
    @NamedQuery(name = "DirectorProyecto.findById", query = "SELECT d FROM DirectorProyecto d WHERE d.id = :id"),
    @NamedQuery(name = "DirectorProyecto.findByFechaInicio", query = "SELECT d FROM DirectorProyecto d WHERE d.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "DirectorProyecto.findByFechaCulminacion", query = "SELECT d FROM DirectorProyecto d WHERE d.fechaCulminacion = :fechaCulminacion")})
public class DirectorProyecto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_culminacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCulminacion;
    @JoinColumn(name = "proyecto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Proyecto proyectoId;
    @JoinColumn(name = "estado_director_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EstadoDirector estadoDirectorId;
    @JoinColumn(name = "director_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Director directorId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "directorProyectoId")
    private List<RenunciaDirector> renunciaDirectorList;
    @Transient
    private boolean esEditado;

    public DirectorProyecto() {
        esEditado = false;
        this.renunciaDirectorList = new ArrayList<>();
    }

    public DirectorProyecto(Long id) {
        this.id = id;
    }

    public DirectorProyecto(Long id, Date fechaInicio, Date fechaCulminacion) {
        this.id = id;
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

    public EstadoDirector getEstadoDirectorId() {
        return estadoDirectorId;
    }

    public void setEstadoDirectorId(EstadoDirector estadoDirectorId) {
        this.estadoDirectorId = estadoDirectorId;
    }

    public Director getDirectorId() {
        return directorId;
    }

    public void setDirectorId(Director directorId) {
        this.directorId = directorId;
    }

    @XmlTransient
    public List<RenunciaDirector> getRenunciaDirectorList() {
        return renunciaDirectorList;
    }

    public void setRenunciaDirectorList(List<RenunciaDirector> renunciaDirectorList) {
        this.renunciaDirectorList = renunciaDirectorList;
    }

    public boolean isEsEditado() {
        return esEditado;
    }

    public void setEsEditado(boolean esEditado) {
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
        if (!(object instanceof DirectorProyecto)) {
            return false;
        }
        DirectorProyecto other = (DirectorProyecto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.DirectorProyecto[ id=" + id + " ]";
    }

}
