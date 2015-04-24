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
@Table(name = "docente_proyecto")
@XmlRootElement
@NamedStoredProcedureQueries({

    @NamedStoredProcedureQuery(
            name = "proyectoSinPertinencia",
            resultClasses = DocenteProyecto.class,
            procedureName = "sp_proyectos_sin_pertinencia",
            parameters = {
                @StoredProcedureParameter(mode = IN, name = "carreraId", type = Integer.class)
            }
    ),
    @NamedStoredProcedureQuery(
            name = "proyectosParaPertinencia",
            resultClasses = DocenteProyecto.class,
            procedureName = "sp_proyectos_para_pertinencia",
            parameters = {
                @StoredProcedureParameter(mode = IN, name = "ci", type = String.class)
            }
    ),
    @NamedStoredProcedureQuery(
            name = "docenteProyectoPorDocenteOferta",
            resultClasses = DocenteProyecto.class,
            procedureName = "sp_docentes_proyecto_periodo",
            parameters = {
                @StoredProcedureParameter(mode = IN, name = "ci", type = String.class),
                @StoredProcedureParameter(mode = IN, name = "ofertaId", type = Integer.class)
            }
    )
})
@NamedQueries({
    @NamedQuery(name = "DocenteProyecto.findAll", query = "SELECT d FROM DocenteProyecto d"),
    @NamedQuery(name = "DocenteProyecto.findById", query = "SELECT d FROM DocenteProyecto d WHERE d.id = :id"),
    @NamedQuery(name = "DocenteProyecto.findByFecha", query = "SELECT d FROM DocenteProyecto d WHERE d.fecha = :fecha"),
    @NamedQuery(name = "DocenteProyecto.findByDocenteId", query = "SELECT d FROM DocenteProyecto d WHERE d.docenteId = :docenteId"),
    @NamedQuery(name = "DocenteProyecto.findByEsActivo", query = "SELECT d FROM DocenteProyecto d WHERE d.esActivo = :esActivo")})
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
    @Column(name = "docente_id")
    private Long docenteId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_activo")
    private boolean esActivo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "docenteProyectoId")
    private List<Pertinencia> pertinenciaList;
    @JoinColumn(name = "proyecto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Proyecto proyectoId;
    @Transient
    private boolean esEditado;

    public DocenteProyecto() {
        this.pertinenciaList = new ArrayList<>();
        this.esEditado = false;
    }

    public DocenteProyecto(Long id) {
        this.id = id;
    }

    public DocenteProyecto(Long id, Date fecha, long docenteId, boolean esActivo) {
        this.id = id;
        this.fecha = fecha;
        this.docenteId = docenteId;
        this.esActivo = esActivo;
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

    public Long getDocenteId() {
        return docenteId;
    }

    public void setDocenteId(Long docenteId) {
        this.docenteId = docenteId;
    }

    public boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(boolean esActivo) {
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
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.DocenteProyecto[ id=" + id + " ]";
    }

    public boolean isEsEditado() {
        return esEditado;
    }

    public void setEsEditado(boolean esEditado) {
        this.esEditado = esEditado;
    }

}
