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
@Table(name = "catalogo_oficio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatalogoOficio.findAll", query = "SELECT c FROM CatalogoOficio c"),
    @NamedQuery(name = "CatalogoOficio.findById", query = "SELECT c FROM CatalogoOficio c WHERE c.id = :id"),
    @NamedQuery(name = "CatalogoOficio.findByNombre", query = "SELECT c FROM CatalogoOficio c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "CatalogoOficio.findByObservacion", query = "SELECT c FROM CatalogoOficio c WHERE c.observacion = :observacion"),
    @NamedQuery(name = "CatalogoOficio.findByEsActivo", query = "SELECT c FROM CatalogoOficio c WHERE c.esActivo = :esActivo"),
    @NamedQuery(name = "CatalogoOficio.findByCodigo", query = "SELECT c FROM CatalogoOficio c WHERE c.codigo = :codigo")})
public class CatalogoOficio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "observacion")
    private String observacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_activo")
    private boolean esActivo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "codigo")
    private String codigo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "catalogoOficioId")
    private List<OficioCarrera> oficioCarreraList;

    public CatalogoOficio() {
    }

    public CatalogoOficio(Integer id) {
        this.id = id;
    }

    public CatalogoOficio(Integer id, String nombre, String observacion, boolean esActivo, String codigo) {
        this.id = id;
        this.nombre = nombre;
        this.observacion = observacion;
        this.esActivo = esActivo;
        this.codigo = codigo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(boolean esActivo) {
        this.esActivo = esActivo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @XmlTransient
    public List<OficioCarrera> getOficioCarreraList() {
        return oficioCarreraList;
    }

    public void setOficioCarreraList(List<OficioCarrera> oficioCarreraList) {
        this.oficioCarreraList = oficioCarreraList;
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
        if (!(object instanceof CatalogoOficio)) {
            return false;
        }
        CatalogoOficio other = (CatalogoOficio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.CatalogoOficio[ id=" + id + " ]";
    }
    
}
