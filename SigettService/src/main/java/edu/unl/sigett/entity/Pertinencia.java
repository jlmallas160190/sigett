/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "pertinencia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pertinencia.findAll", query = "SELECT p FROM Pertinencia p"),
    @NamedQuery(name = "Pertinencia.findById", query = "SELECT p FROM Pertinencia p WHERE p.id = :id"),
    @NamedQuery(name = "Pertinencia.findByFecha", query = "SELECT p FROM Pertinencia p WHERE p.fecha = :fecha"),
    @NamedQuery(name = "Pertinencia.findByObservacion", query = "SELECT p FROM Pertinencia p WHERE p.observacion = :observacion"),
    @NamedQuery(name = "Pertinencia.findByEsActivo", query = "SELECT p FROM Pertinencia p WHERE p.esActivo = :esActivo"),
    @NamedQuery(name = "Pertinencia.findByEsAceptado", query = "SELECT p FROM Pertinencia p WHERE p.esAceptado = :esAceptado")})
public class Pertinencia implements Serializable {
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
    @Size(min = 1, max = 1000)
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "es_activo")
    private Boolean esActivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_aceptado")
    private boolean esAceptado;
    @JoinColumn(name = "docente_proyecto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DocenteProyecto docenteProyectoId;

    public Pertinencia() {
    }

    public Pertinencia(Long id) {
        this.id = id;
    }

    public Pertinencia(Long id, Date fecha, String observacion, boolean esAceptado) {
        this.id = id;
        this.fecha = fecha;
        this.observacion = observacion;
        this.esAceptado = esAceptado;
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

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(Boolean esActivo) {
        this.esActivo = esActivo;
    }

    public boolean getEsAceptado() {
        return esAceptado;
    }

    public void setEsAceptado(boolean esAceptado) {
        this.esAceptado = esAceptado;
    }

    public DocenteProyecto getDocenteProyectoId() {
        return docenteProyectoId;
    }

    public void setDocenteProyectoId(DocenteProyecto docenteProyectoId) {
        this.docenteProyectoId = docenteProyectoId;
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
        if (!(object instanceof Pertinencia)) {
            return false;
        }
        Pertinencia other = (Pertinencia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.Pertinencia[ id=" + id + " ]";
    }
    
}
