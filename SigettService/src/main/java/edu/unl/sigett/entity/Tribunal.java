/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.entity;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "tribunal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tribunal.findAll", query = "SELECT t FROM Tribunal t"),
    @NamedQuery(name = "Tribunal.findById", query = "SELECT t FROM Tribunal t WHERE t.id = :id"),
    @NamedQuery(name = "Tribunal.findByDescripcion", query = "SELECT t FROM Tribunal t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "Tribunal.findByEsActivo", query = "SELECT t FROM Tribunal t WHERE t.esActivo = :esActivo")})
public class Tribunal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_activo")
    private boolean esActivo;
    @JoinColumn(name = "proyecto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Proyecto proyectoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tribunalId")
    private List<Miembro> miembroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tribunalId")
    private List<EvaluacionTribunal> evaluacionTribunalList;

    public Tribunal() {
    }

    public Tribunal(Long id) {
        this.id = id;
    }

    public Tribunal(Long id, String descripcion, boolean esActivo) {
        this.id = id;
        this.descripcion = descripcion;
        this.esActivo = esActivo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(boolean esActivo) {
        this.esActivo = esActivo;
    }

    public Proyecto getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Proyecto proyectoId) {
        this.proyectoId = proyectoId;
    }

    @XmlTransient
    public List<Miembro> getMiembroList() {
        return miembroList;
    }

    public void setMiembroList(List<Miembro> miembroList) {
        this.miembroList = miembroList;
    }

    @XmlTransient
    public List<EvaluacionTribunal> getEvaluacionTribunalList() {
        return evaluacionTribunalList;
    }

    public void setEvaluacionTribunalList(List<EvaluacionTribunal> evaluacionTribunalList) {
        this.evaluacionTribunalList = evaluacionTribunalList;
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
        if (!(object instanceof Tribunal)) {
            return false;
        }
        Tribunal other = (Tribunal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.Tribunal[ id=" + id + " ]";
    }
    
}
