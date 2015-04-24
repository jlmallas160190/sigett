/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jlmallas.soporte.entity;

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
@Table(name = "excepcion",schema = "soporte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Excepcion.findAll", query = "SELECT e FROM Excepcion e"),
    @NamedQuery(name = "Excepcion.findById", query = "SELECT e FROM Excepcion e WHERE e.id = :id"),
    @NamedQuery(name = "Excepcion.findByUsuario", query = "SELECT e FROM Excepcion e WHERE e.usuario = :usuario"),
    @NamedQuery(name = "Excepcion.findByDescripcion", query = "SELECT e FROM Excepcion e WHERE e.descripcion = :descripcion"),
    @NamedQuery(name = "Excepcion.findByFecha", query = "SELECT e FROM Excepcion e WHERE e.fecha = :fecha")})
public class Excepcion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "usuario")
    private String usuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @JoinColumn(name = "objeto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Objeto objetoId;

    public Excepcion() {
    }

    public Excepcion(Long id) {
        this.id = id;
    }

    public Excepcion(Long id, String usuario, String descripcion, Date fecha) {
        this.id = id;
        this.usuario = usuario;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Objeto getObjetoId() {
        return objetoId;
    }

    public void setObjetoId(Objeto objetoId) {
        this.objetoId = objetoId;
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
        if (!(object instanceof Excepcion)) {
            return false;
        }
        Excepcion other = (Excepcion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jlmallas.soporte.entity.Excepcion[ id=" + id + " ]";
    }
    
}
