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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "linea_investigacion_proyecto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LineaInvestigacionProyecto.findAll", query = "SELECT l FROM LineaInvestigacionProyecto l"),
    @NamedQuery(name = "LineaInvestigacionProyecto.findById", query = "SELECT l FROM LineaInvestigacionProyecto l WHERE l.id = :id")})
public class LineaInvestigacionProyecto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "proyecto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Proyecto proyectoId;
    @JoinColumn(name = "linea_investigacion_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LineaInvestigacion lineaInvestigacionId;

    public LineaInvestigacionProyecto() {
    }

    public LineaInvestigacionProyecto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Proyecto getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Proyecto proyectoId) {
        this.proyectoId = proyectoId;
    }

    public LineaInvestigacion getLineaInvestigacionId() {
        return lineaInvestigacionId;
    }

    public void setLineaInvestigacionId(LineaInvestigacion lineaInvestigacionId) {
        this.lineaInvestigacionId = lineaInvestigacionId;
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
        if (!(object instanceof LineaInvestigacionProyecto)) {
            return false;
        }
        LineaInvestigacionProyecto other = (LineaInvestigacionProyecto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.LineaInvestigacionProyecto[ id=" + id + " ]";
    }
    
}
