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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "estudiante_usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstudianteUsuario.findAll", query = "SELECT e FROM EstudianteUsuario e"),
    @NamedQuery(name = "EstudianteUsuario.findById", query = "SELECT e FROM EstudianteUsuario e WHERE e.id = :id"),
    @NamedQuery(name = "EstudianteUsuario.findByEstudianteId", query = "SELECT e FROM EstudianteUsuario e WHERE e.estudianteId = :estudianteId")})
public class EstudianteUsuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estudiante_id")
    private Long estudianteId;

    public EstudianteUsuario() {
    }

    public EstudianteUsuario(Long estudianteId) {
        this.estudianteId = estudianteId;
    }

    public EstudianteUsuario(Long id, long estudianteId) {
        this.id = id;
        this.estudianteId = estudianteId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(Long estudianteId) {
        this.estudianteId = estudianteId;
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
        if (!(object instanceof EstudianteUsuario)) {
            return false;
        }
        EstudianteUsuario other = (EstudianteUsuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.EstudianteUsuario[ id=" + id + " ]";
    }

}
