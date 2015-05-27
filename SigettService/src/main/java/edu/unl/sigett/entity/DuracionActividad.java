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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "duracion_actividad")
@XmlRootElement
public class DuracionActividad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "duracion_id")
    private Long duracionId;
    @JoinColumn(name = "actividad_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Actividad actividadId;

    public DuracionActividad() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDuracionId() {
        return duracionId;
    }

    public void setDuracionId(Long duracionId) {
        this.duracionId = duracionId;
    }

    public Actividad getActividadId() {
        return actividadId;
    }

    public void setActividadId(Actividad actividadId) {
        this.actividadId = actividadId;
    }
    
}
