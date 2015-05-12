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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "titulo_docente", schema = "academico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TituloDocente.findAll", query = "SELECT t FROM TituloDocente t"),
    @NamedQuery(name = "TituloDocente.findById", query = "SELECT t FROM TituloDocente t WHERE t.id = :id")})
public class TituloDocente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tituloDocenteId")
    private List<Docente> docenteList;
    @JoinColumn(name = "titulo_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Titulo tituloId;

    public TituloDocente() {
    }

    public TituloDocente(Titulo tituloId) {
        this.tituloId = tituloId;
    }

    public TituloDocente(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlTransient
    public List<Docente> getDocenteList() {
        return docenteList;
    }

    public void setDocenteList(List<Docente> docenteList) {
        this.docenteList = docenteList;
    }

    public Titulo getTituloId() {
        return tituloId;
    }

    public void setTituloId(Titulo tituloId) {
        this.tituloId = tituloId;
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
        if (!(object instanceof TituloDocente)) {
            return false;
        }
        TituloDocente other = (TituloDocente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
       return id + ": " + tituloId.getNombre();
    }
    
}
