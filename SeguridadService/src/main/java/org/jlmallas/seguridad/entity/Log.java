/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.seguridad.entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author JorgeLuis
 */
@Entity
@Table(name = "log", schema = "seguridad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Log.findAll", query = "SELECT l FROM Log l"),
    @NamedQuery(name = "Log.findById", query = "SELECT l FROM Log l WHERE l.id = :id"),
    @NamedQuery(name = "Log.findByFechaAccion", query = "SELECT l FROM Log l WHERE l.fechaAccion = :fechaAccion"),
    @NamedQuery(name = "Log.findByTabla", query = "SELECT l FROM Log l WHERE l.tabla = :tabla"),
    @NamedQuery(name = "Log.findByTablaId", query = "SELECT l FROM Log l WHERE l.tablaId = :tablaId"),
    @NamedQuery(name = "Log.findByInforme", query = "SELECT l FROM Log l WHERE l.informe = :informe"),
    @NamedQuery(name = "Log.findByAccion", query = "SELECT l FROM Log l WHERE l.accion = :accion")})
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_accion")
    @Temporal(TemporalType.DATE)
    private Date fechaAccion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hora_accion")
    @Temporal(TemporalType.TIME)
    private Date horaAccion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "tabla")
    private String tabla;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "tabla_id")
    private String tablaId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5000)
    @Column(name = "informe")
    private String informe;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "accion")
    private String accion;
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario usuarioId;

    public Log() {
    }

    public Log(Long id) {
        this.id = id;
    }

    public Log(Long id, Date fechaAccion, String tabla, String tablaId, String informe, String accion) {
        this.id = id;
        this.fechaAccion = fechaAccion;
        this.tabla = tabla;
        this.tablaId = tablaId;
        this.informe = informe;
        this.accion = accion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaAccion() {
        return fechaAccion;
    }

    public void setFechaAccion(Date fechaAccion) {
        this.fechaAccion = fechaAccion;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getTablaId() {
        return tablaId;
    }

    public void setTablaId(String tablaId) {
        this.tablaId = tablaId;
    }

    public String getInforme() {
        return informe;
    }

    public void setInforme(String informe) {
        this.informe = informe;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Usuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Usuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Date getHoraAccion() {
        return horaAccion;
    }

    public void setHoraAccion(Date horaAccion) {
        this.horaAccion = horaAccion;
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
        if (!(object instanceof Log)) {
            return false;
        }
        Log other = (Log) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Log[ id=" + id + " ]";
    }

}
