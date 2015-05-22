/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
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
@Table(name = "periodo_coordinacion", schema = "academico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PeriodoCoordinacion.findAll", query = "SELECT p FROM PeriodoCoordinacion p"),
    @NamedQuery(name = "PeriodoCoordinacion.findById", query = "SELECT p FROM PeriodoCoordinacion p WHERE p.id = :id"),
    @NamedQuery(name = "PeriodoCoordinacion.findByFechaInicio", query = "SELECT p FROM PeriodoCoordinacion p WHERE p.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "PeriodoCoordinacion.findByFechaFin", query = "SELECT p FROM PeriodoCoordinacion p WHERE p.fechaFin = :fechaFin"),
    @NamedQuery(name = "PeriodoCoordinacion.findByEsActivo", query = "SELECT p FROM PeriodoCoordinacion p WHERE p.esActivo = :esActivo")})
public class PeriodoCoordinacion implements Serializable {

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
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_activo")
    private Boolean esActivo;
    @OneToMany(mappedBy = "periodoId")
    private List<CoordinadorPeriodo> coordinadorPeriodoList;
    @JoinColumn(name = "carrera_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Carrera carreraId;

    public PeriodoCoordinacion() {
        this.esActivo = true;
    }

    public PeriodoCoordinacion(Long id) {
        this.id = id;
    }

    public PeriodoCoordinacion(Carrera carrera,Boolean esActivo) {
        this.carreraId=carrera;
        this.esActivo = esActivo;
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

    public Boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(Boolean esActivo) {
        this.esActivo = esActivo;
    }

    @XmlTransient
    public List<CoordinadorPeriodo> getCoordinadorPeriodoList() {
        return coordinadorPeriodoList;
    }

    public void setCoordinadorPeriodoList(List<CoordinadorPeriodo> coordinadorPeriodoList) {
        this.coordinadorPeriodoList = coordinadorPeriodoList;
    }

    public Carrera getCarreraId() {
        return carreraId;
    }

    public void setCarreraId(Carrera carreraId) {
        this.carreraId = carreraId;
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
        if (!(object instanceof PeriodoCoordinacion)) {
            return false;
        }
        PeriodoCoordinacion other = (PeriodoCoordinacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        SimpleDateFormat formateador = new SimpleDateFormat("EEEEE dd MMMMM yyyy");
        String fi = formateador.format(fechaInicio);
        String ff = formateador.format(fechaFin);
        return this.id + ": " + fi + "-" + ff;

    }

}
