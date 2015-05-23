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
@Table(name = "linea_investigacion_carrera")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LineaInvestigacionCarrera.findAll", query = "SELECT l FROM LineaInvestigacionCarrera l"),
    @NamedQuery(name = "LineaInvestigacionCarrera.findById", query = "SELECT l FROM LineaInvestigacionCarrera l WHERE l.id = :id"),
    @NamedQuery(name = "LineaInvestigacionCarrera.findByCarreraId", query = "SELECT l FROM LineaInvestigacionCarrera l WHERE l.carreraId = :carreraId")})
public class LineaInvestigacionCarrera implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "carrera_id")
    private Integer carreraId;
    @JoinColumn(name = "linea_investigacion_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LineaInvestigacion lineaInvestigacionId;
    private Integer count;

    public LineaInvestigacionCarrera() {
    }

    public LineaInvestigacionCarrera(LineaInvestigacion li, Integer carrera) {
        this.lineaInvestigacionId = li;
        this.carreraId = carrera;
    }

    public LineaInvestigacionCarrera(Long id, int carreraId) {
        this.id = id;
        this.carreraId = carreraId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCarreraId() {
        return carreraId;
    }

    public void setCarreraId(Integer carreraId) {
        this.carreraId = carreraId;
    }

    public LineaInvestigacion getLineaInvestigacionId() {
        return lineaInvestigacionId;
    }

    public void setLineaInvestigacionId(LineaInvestigacion lineaInvestigacionId) {
        this.lineaInvestigacionId = lineaInvestigacionId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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
        if (!(object instanceof LineaInvestigacionCarrera)) {
            return false;
        }
        LineaInvestigacionCarrera other = (LineaInvestigacionCarrera) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.LineaInvestigacionCarrera[ id=" + id + " ]";
    }

}
