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
@Table(name = "usuario_carrera")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuarioCarrera.findAll", query = "SELECT u FROM UsuarioCarrera u"),
    @NamedQuery(name = "UsuarioCarrera.findById", query = "SELECT u FROM UsuarioCarrera u WHERE u.id = :id"),
    @NamedQuery(name = "UsuarioCarrera.findByUsuarioId", query = "SELECT u FROM UsuarioCarrera u WHERE u.usuarioId = :usuarioId"),
    @NamedQuery(name = "UsuarioCarrera.findByCarreraId", query = "SELECT u FROM UsuarioCarrera u WHERE u.carreraId = :carreraId")})
public class UsuarioCarrera implements Serializable {

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
    @Basic(optional = false)
    @NotNull
    @Column(name = "usuario_id")
    private Long usuarioId;

    public UsuarioCarrera() {
    }

    public UsuarioCarrera(Long id) {
        this.id = id;
    }

    public UsuarioCarrera(Long id, int carreraId) {
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

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
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
        if (!(object instanceof UsuarioCarrera)) {
            return false;
        }
        UsuarioCarrera other = (UsuarioCarrera) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.UsuarioCarrera[ id=" + id + " ]";
    }

}
