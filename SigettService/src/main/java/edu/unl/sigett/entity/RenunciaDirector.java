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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "renuncia_director")
@XmlRootElement
public class RenunciaDirector implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Renuncia renuncia;
    @JoinColumn(name = "director_proyecto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DirectorProyecto directorProyectoId;

    public RenunciaDirector() {
    }

    public RenunciaDirector(Long id) {
        this.id = id;
    }

    public RenunciaDirector(Long id, Renuncia renuncia, DirectorProyecto directorProyectoId) {
        this.id = id;
        this.renuncia = renuncia;
        this.directorProyectoId = directorProyectoId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Renuncia getRenuncia() {
        return renuncia;
    }

    public void setRenuncia(Renuncia renuncia) {
        this.renuncia = renuncia;
    }

    public DirectorProyecto getDirectorProyectoId() {
        return directorProyectoId;
    }

    public void setDirectorProyectoId(DirectorProyecto directorProyectoId) {
        this.directorProyectoId = directorProyectoId;
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
        if (!(object instanceof RenunciaDirector)) {
            return false;
        }
        RenunciaDirector other = (RenunciaDirector) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.RenunciaDirector[ id=" + id + " ]";
    }

}
