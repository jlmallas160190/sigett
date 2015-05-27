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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "duracion_cronograma")
@XmlRootElement
public class DuracionCronograma implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "duracion_id")
    private Long duracionId;
    @JoinColumn(name = "cronograma_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Cronograma cronogramaId;

    public DuracionCronograma() {
    }

    public DuracionCronograma(Long id) {
        this.id = id;
    }

   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDuracionId() {
        return duracionId;
    }

    public void setDuracionId(Long duracionId) {
        this.duracionId = duracionId;
    }

  
    public Cronograma getCronogramaId() {
        return cronogramaId;
    }

    public void setCronogramaId(Cronograma cronogramaId) {
        this.cronogramaId = cronogramaId;
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
        if (!(object instanceof DuracionCronograma)) {
            return false;
        }
        DuracionCronograma other = (DuracionCronograma) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.DuracionCronograma[ id=" + id + " ]";
    }
    
}
