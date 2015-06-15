/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
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
@Table(name = "evento_persona", schema = "comun")
@XmlRootElement
@Cacheable(value = false)
public class EventoPersona implements Serializable {

    @Id
    @Column(name = "id")
    @Basic(optional = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "persona_id", referencedColumnName = "id")
    private Persona personaId;
    @Column(name = "tabla_id")
    private Long tablaId;

    public EventoPersona() {
    }

    public EventoPersona(Long id) {
        this.id = id;
    }

    public EventoPersona(Long id, Persona personaId, Long tablaId) {
        this.id = id;
        this.personaId = personaId;
        this.tablaId = tablaId;
    }

    public Persona getPersonaId() {
        return personaId;
    }

    public void setPersonaId(Persona personaId) {
        this.personaId = personaId;
    }

    public Long getTablaId() {
        return tablaId;
    }

    public void setTablaId(Long tablaId) {
        this.tablaId = tablaId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
