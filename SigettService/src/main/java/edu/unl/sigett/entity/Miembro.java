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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "miembro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Miembro.findAll", query = "SELECT m FROM Miembro m"),
    @NamedQuery(name = "Miembro.findById", query = "SELECT m FROM Miembro m WHERE m.id = :id"),
    @NamedQuery(name = "Miembro.findByDocenteId", query = "SELECT m FROM Miembro m WHERE m.docenteId = :docenteId"),
    @NamedQuery(name = "Miembro.findByEsActivo", query = "SELECT m FROM Miembro m WHERE m.esActivo = :esActivo")})
public class Miembro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "docente_id")
    private Long docenteId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_activo")
    private boolean esActivo;
    @JoinColumn(name = "tribunal_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tribunal tribunalId;
    @JoinColumn(name = "cargo_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CargoMiembro cargoId;

    public Miembro() {
    }

    public Miembro(Long id) {
        this.id = id;
    }

    public Miembro(Long id, long docenteId, boolean esActivo) {
        this.id = id;
        this.docenteId = docenteId;
        this.esActivo = esActivo;
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

    public boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(boolean esActivo) {
        this.esActivo = esActivo;
    }

    public Tribunal getTribunalId() {
        return tribunalId;
    }

    public void setTribunalId(Tribunal tribunalId) {
        this.tribunalId = tribunalId;
    }

    public CargoMiembro getCargoId() {
        return cargoId;
    }

    public void setCargoId(CargoMiembro cargoId) {
        this.cargoId = cargoId;
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
        if (!(object instanceof Miembro)) {
            return false;
        }
        Miembro other = (Miembro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.Miembro[ id=" + id + " ]";
    }
    
}
