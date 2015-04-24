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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "sugerencia_calificacion_miembro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SugerenciaCalificacionMiembro.findAll", query = "SELECT s FROM SugerenciaCalificacionMiembro s"),
    @NamedQuery(name = "SugerenciaCalificacionMiembro.findById", query = "SELECT s FROM SugerenciaCalificacionMiembro s WHERE s.id = :id"),
    @NamedQuery(name = "SugerenciaCalificacionMiembro.findByDescripcion", query = "SELECT s FROM SugerenciaCalificacionMiembro s WHERE s.descripcion = :descripcion"),
    @NamedQuery(name = "SugerenciaCalificacionMiembro.findByEsActivo", query = "SELECT s FROM SugerenciaCalificacionMiembro s WHERE s.esActivo = :esActivo")})
public class SugerenciaCalificacionMiembro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_activo")
    private boolean esActivo;
    @JoinColumn(name = "calificacion_miembro_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CalificacionMiembro calificacionMiembroId;

    public SugerenciaCalificacionMiembro() {
    }

    public SugerenciaCalificacionMiembro(Long id) {
        this.id = id;
    }

    public SugerenciaCalificacionMiembro(Long id, String descripcion, boolean esActivo) {
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

    public CalificacionMiembro getCalificacionMiembroId() {
        return calificacionMiembroId;
    }

    public void setCalificacionMiembroId(CalificacionMiembro calificacionMiembroId) {
        this.calificacionMiembroId = calificacionMiembroId;
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
        if (!(object instanceof SugerenciaCalificacionMiembro)) {
            return false;
        }
        SugerenciaCalificacionMiembro other = (SugerenciaCalificacionMiembro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.SugerenciaCalificacionMiembro[ id=" + id + " ]";
    }
    
}
