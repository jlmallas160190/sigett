/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jlmallas.soporte.entity;

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
@Table(name = "encargado_proyecto_software",schema = "soporte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EncargadoProyectoSoftware.findAll", query = "SELECT e FROM EncargadoProyectoSoftware e"),
    @NamedQuery(name = "EncargadoProyectoSoftware.findById", query = "SELECT e FROM EncargadoProyectoSoftware e WHERE e.id = :id")})
public class EncargadoProyectoSoftware implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "proyecto_software_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ProyectoSoftware proyectoSoftwareId;
    @JoinColumn(name = "encargado_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Encargado encargadoId;

    public EncargadoProyectoSoftware() {
    }

    public EncargadoProyectoSoftware(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProyectoSoftware getProyectoSoftwareId() {
        return proyectoSoftwareId;
    }

    public void setProyectoSoftwareId(ProyectoSoftware proyectoSoftwareId) {
        this.proyectoSoftwareId = proyectoSoftwareId;
    }

    public Encargado getEncargadoId() {
        return encargadoId;
    }

    public void setEncargadoId(Encargado encargadoId) {
        this.encargadoId = encargadoId;
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
        if (!(object instanceof EncargadoProyectoSoftware)) {
            return false;
        }
        EncargadoProyectoSoftware other = (EncargadoProyectoSoftware) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jlmallas.soporte.entity.EncargadoProyectoSoftware[ id=" + id + " ]";
    }
    
}
