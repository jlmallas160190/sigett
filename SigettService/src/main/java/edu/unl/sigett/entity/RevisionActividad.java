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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "revision_actividad")
@XmlRootElement
public class RevisionActividad implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "actividad_id", referencedColumnName = "id")
    @ManyToOne
    private Actividad actividadId;

    public RevisionActividad() {
    }

    public RevisionActividad(Long id) {
        this.id = id;
    }

    public RevisionActividad(Long id, Actividad actividadId) {
        this.id = id;
        this.actividadId = actividadId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Actividad getActividadId() {
        return actividadId;
    }

    public void setActividadId(Actividad actividadId) {
        this.actividadId = actividadId;
    }

}
