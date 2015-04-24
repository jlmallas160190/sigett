/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
 * @author jorge-luis
 */
@Entity
@Table(name = "oficio_carrera")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OficioCarrera.findAll", query = "SELECT o FROM OficioCarrera o"),
    @NamedQuery(name = "OficioCarrera.findById", query = "SELECT o FROM OficioCarrera o WHERE o.id = :id"),
    @NamedQuery(name = "OficioCarrera.findByNumeroOficio", query = "SELECT o FROM OficioCarrera o WHERE o.numeroOficio = :numeroOficio"),
    @NamedQuery(name = "OficioCarrera.findByFecha", query = "SELECT o FROM OficioCarrera o WHERE o.fecha = :fecha"),
    @NamedQuery(name = "OficioCarrera.findByEsActivo", query = "SELECT o FROM OficioCarrera o WHERE o.esActivo = :esActivo"),
    @NamedQuery(name = "OficioCarrera.findByCarreraId", query = "SELECT o FROM OficioCarrera o WHERE o.carreraId = :carreraId"),
    @NamedQuery(name = "OficioCarrera.findByTablaOficioId", query = "SELECT o FROM OficioCarrera o WHERE o.tablaOficioId = :tablaOficioId")})
public class OficioCarrera implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "numero_oficio")
    private String numeroOficio;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "oficio")
    private byte[] oficio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_activo")
    private boolean esActivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "carrera_id")
    private int carreraId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tabla_oficio_id")
    private long tablaOficioId;
    @JoinColumn(name = "catalogo_oficio_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CatalogoOficio catalogoOficioId;

    public OficioCarrera() {
    }

    public OficioCarrera(Long id) {
        this.id = id;
    }

    public OficioCarrera(Long id, String numeroOficio, byte[] oficio, Date fecha, boolean esActivo, int carreraId, long tablaOficioId) {
        this.id = id;
        this.numeroOficio = numeroOficio;
        this.oficio = oficio;
        this.fecha = fecha;
        this.esActivo = esActivo;
        this.carreraId = carreraId;
        this.tablaOficioId = tablaOficioId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroOficio() {
        return numeroOficio;
    }

    public void setNumeroOficio(String numeroOficio) {
        this.numeroOficio = numeroOficio;
    }

    public byte[] getOficio() {
        return oficio;
    }

    public void setOficio(byte[] oficio) {
        this.oficio = oficio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(boolean esActivo) {
        this.esActivo = esActivo;
    }

    public int getCarreraId() {
        return carreraId;
    }

    public void setCarreraId(int carreraId) {
        this.carreraId = carreraId;
    }

    public long getTablaOficioId() {
        return tablaOficioId;
    }

    public void setTablaOficioId(long tablaOficioId) {
        this.tablaOficioId = tablaOficioId;
    }

    public CatalogoOficio getCatalogoOficioId() {
        return catalogoOficioId;
    }

    public void setCatalogoOficioId(CatalogoOficio catalogoOficioId) {
        this.catalogoOficioId = catalogoOficioId;
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
        if (!(object instanceof OficioCarrera)) {
            return false;
        }
        OficioCarrera other = (OficioCarrera) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.OficioCarrera[ id=" + id + " ]";
    }
    
}
