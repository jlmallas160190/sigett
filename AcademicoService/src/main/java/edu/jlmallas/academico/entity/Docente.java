/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.jlmallas.academico.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "docente", schema = "academico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Docente.findAll", query = "SELECT d FROM Docente d"),
    @NamedQuery(name = "Docente.findById", query = "SELECT d FROM Docente d WHERE d.id = :id")})
public class Docente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "docenteId")
    private List<DocenteCarrera> docenteCarreraList;
    @JoinColumn(name = "titulo_docente_id", referencedColumnName = "id")
    @ManyToOne
    private TituloDocente tituloDocenteId;
    @JoinColumn(name = "estado_laboral_id", referencedColumnName = "id")
    @ManyToOne
    private EstadoLaboral estadoLaboralId;

    public Docente() {
    }

    public Docente(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlTransient
    public List<DocenteCarrera> getDocenteCarreraList() {
        return docenteCarreraList;
    }

    public void setDocenteCarreraList(List<DocenteCarrera> docenteCarreraList) {
        this.docenteCarreraList = docenteCarreraList;
    }

    public TituloDocente getTituloDocenteId() {
        return tituloDocenteId;
    }

    public void setTituloDocenteId(TituloDocente tituloDocenteId) {
        this.tituloDocenteId = tituloDocenteId;
    }

    public EstadoLaboral getEstadoLaboralId() {
        return estadoLaboralId;
    }

    public void setEstadoLaboralId(EstadoLaboral estadoLaboralId) {
        this.estadoLaboralId = estadoLaboralId;
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
        if (!(object instanceof Docente)) {
            return false;
        }
        Docente other = (Docente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.jlmallas.academico.entity.Docente[ id=" + id + " ]";
    }
    
}
