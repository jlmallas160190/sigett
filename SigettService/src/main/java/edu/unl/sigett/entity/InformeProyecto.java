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
@Table(name = "informe_proyecto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InformeProyecto.findAll", query = "SELECT i FROM InformeProyecto i"),
    @NamedQuery(name = "InformeProyecto.findById", query = "SELECT i FROM InformeProyecto i WHERE i.id = :id"),
    @NamedQuery(name = "InformeProyecto.findByFecha", query = "SELECT i FROM InformeProyecto i WHERE i.fecha = :fecha"),
    @NamedQuery(name = "InformeProyecto.findByObservacion", query = "SELECT i FROM InformeProyecto i WHERE i.observacion = :observacion")})
public class InformeProyecto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "observacion")
    private String observacion;
    @JoinColumn(name = "proyecto_id", referencedColumnName = "id")
    @ManyToOne
    private Proyecto proyectoId;
    @JoinColumn(name = "catalogo_informe_proyecto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CatalogoInformeProyecto catalogoInformeProyectoId;

    public InformeProyecto() {
    }

    public InformeProyecto(Long id) {
        this.id = id;
    }

    public InformeProyecto(Long id, String observacion) {
        this.id = id;
        this.observacion = observacion;
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

    public Proyecto getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Proyecto proyectoId) {
        this.proyectoId = proyectoId;
    }

    public CatalogoInformeProyecto getCatalogoInformeProyectoId() {
        return catalogoInformeProyectoId;
    }

    public void setCatalogoInformeProyectoId(CatalogoInformeProyecto catalogoInformeProyectoId) {
        this.catalogoInformeProyectoId = catalogoInformeProyectoId;
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
        if (!(object instanceof InformeProyecto)) {
            return false;
        }
        InformeProyecto other = (InformeProyecto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.InformeProyecto[ id=" + id + " ]";
    }
    
}
