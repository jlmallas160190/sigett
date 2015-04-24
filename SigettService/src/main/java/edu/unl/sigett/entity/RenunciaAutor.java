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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "renuncia_autor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RenunciaAutor.findAll", query = "SELECT r FROM RenunciaAutor r"),
    @NamedQuery(name = "RenunciaAutor.findById", query = "SELECT r FROM RenunciaAutor r WHERE r.id = :id")})
public class RenunciaAutor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Renuncia renuncia;
    @JoinColumn(name = "autor_proyecto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AutorProyecto autorProyectoId;
    @Transient
    private boolean esEditado;

    public RenunciaAutor() {
        this.esEditado = false;
    }

    public RenunciaAutor(Long id) {
        this.id = id;
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

    public AutorProyecto getAutorProyectoId() {
        return autorProyectoId;
    }

    public void setAutorProyectoId(AutorProyecto autorProyectoId) {
        this.autorProyectoId = autorProyectoId;
    }

    public boolean isEsEditado() {
        return esEditado;
    }

    public void setEsEditado(boolean esEditado) {
        this.esEditado = esEditado;
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
        if (!(object instanceof RenunciaAutor)) {
            return false;
        }
        RenunciaAutor other = (RenunciaAutor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.RenunciaAutor[ id=" + id + " ]";
    }

}
