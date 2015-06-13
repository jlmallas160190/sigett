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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "evento_carrera_id")
@XmlRootElement
public class EventoDocenteCarrera implements Serializable {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "docente_carrera_id")
    private Long docenteCarreraId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "eventoId")
    private Long eventoId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tablaId")
    private Long tablaId;

    public EventoDocenteCarrera() {
    }

    public EventoDocenteCarrera(Long id) {
        this.id = id;
    }

    public EventoDocenteCarrera(Long id, Long docenteCarreraId, Long eventoId, Long tablaId) {
        this.id = id;
        this.docenteCarreraId = docenteCarreraId;
        this.eventoId = eventoId;
        this.tablaId = tablaId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocenteCarreraId() {
        return docenteCarreraId;
    }

    public void setDocenteCarreraId(Long docenteCarreraId) {
        this.docenteCarreraId = docenteCarreraId;
    }

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

    public Long getTablaId() {
        return tablaId;
    }

    public void setTablaId(Long tablaId) {
        this.tablaId = tablaId;
    }

}
