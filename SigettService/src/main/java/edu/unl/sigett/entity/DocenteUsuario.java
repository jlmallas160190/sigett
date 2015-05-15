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
@Table(name = "docente_usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocenteUsuario.findAll", query = "SELECT d FROM DocenteUsuario d"),
    @NamedQuery(name = "DocenteUsuario.findById", query = "SELECT d FROM DocenteUsuario d WHERE d.id = :id"),
    @NamedQuery(name = "DocenteUsuario.findByDocenteId", query = "SELECT d FROM DocenteUsuario d WHERE d.docenteId = :docenteId")
})
public class DocenteUsuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "docente_id")
    private Long docenteId;

    public DocenteUsuario() {
    }

    public DocenteUsuario(Long id, Long docenteId) {
        this.id = id;
        this.docenteId = docenteId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocenteId() {
        return docenteId;
    }

    public void setDocenteId(Long docenteId) {
        this.docenteId = docenteId;
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
        if (!(object instanceof DocenteUsuario)) {
            return false;
        }
        DocenteUsuario other = (DocenteUsuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.DocenteUsuario[ id=" + id + " ]";
    }

}
