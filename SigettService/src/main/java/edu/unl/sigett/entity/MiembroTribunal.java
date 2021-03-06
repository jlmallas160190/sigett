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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "miembro_tribunal")
@XmlRootElement
public class MiembroTribunal implements Serializable {

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
    private Boolean esActivo;
    @JoinColumn(name = "tribunal_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tribunal tribunalId;
    @Column(name = "cargo_id")
    private Long cargoId;
    @Transient
    private String cargo;

    public MiembroTribunal() {
    }

    public MiembroTribunal(Long id) {
        this.id = id;
    }

    public MiembroTribunal(Long id, Long docenteId, Boolean esActivo, Tribunal tribunalId, Long cargoId,String cargo) {
        this.id = id;
        this.docenteId = docenteId;
        this.esActivo = esActivo;
        this.tribunalId = tribunalId;
        this.cargoId = cargoId;
        this.cargo=cargo;
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

    public Boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(Boolean esActivo) {
        this.esActivo = esActivo;
    }

    public Tribunal getTribunalId() {
        return tribunalId;
    }

    public void setTribunalId(Tribunal tribunalId) {
        this.tribunalId = tribunalId;
    }

    public Long getCargoId() {
        return cargoId;
    }

    public void setCargoId(Long cargoId) {
        this.cargoId = cargoId;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
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
        if (!(object instanceof MiembroTribunal)) {
            return false;
        }
        MiembroTribunal other = (MiembroTribunal) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.Miembro[ id=" + id + " ]";
    }

}
