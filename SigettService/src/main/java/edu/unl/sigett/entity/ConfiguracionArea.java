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
@Table(name = "configuracion_area")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConfiguracionArea.findAll", query = "SELECT c FROM ConfiguracionArea c"),
    @NamedQuery(name = "ConfiguracionArea.findById", query = "SELECT c FROM ConfiguracionArea c WHERE c.id = :id"),
    @NamedQuery(name = "ConfiguracionArea.findByNombre", query = "SELECT c FROM ConfiguracionArea c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "ConfiguracionArea.findByValor", query = "SELECT c FROM ConfiguracionArea c WHERE c.valor = :valor"),
    @NamedQuery(name = "ConfiguracionArea.findByTipo", query = "SELECT c FROM ConfiguracionArea c WHERE c.tipo = :tipo"),
    @NamedQuery(name = "ConfiguracionArea.findByAreaId", query = "SELECT c FROM ConfiguracionArea c WHERE c.areaId = :areaId"),
    @NamedQuery(name = "ConfiguracionArea.findByCodigo", query = "SELECT c FROM ConfiguracionArea c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "ConfiguracionArea.findByObservacion", query = "SELECT c FROM ConfiguracionArea c WHERE c.observacion = :observacion")})
public class ConfiguracionArea implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "valor")
    private String valor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area_id")
    private int areaId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "observacion")
    private String observacion;

    public ConfiguracionArea() {
    }

    public ConfiguracionArea(Integer id) {
        this.id = id;
    }

    public ConfiguracionArea(Integer id, String nombre, String valor, String tipo, int areaId, String codigo, String observacion) {
        this.id = id;
        this.nombre = nombre;
        this.valor = valor;
        this.tipo = tipo;
        this.areaId = areaId;
        this.codigo = codigo;
        this.observacion = observacion;
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

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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
        if (!(object instanceof ConfiguracionArea)) {
            return false;
        }
        ConfiguracionArea other = (ConfiguracionArea) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.ConfiguracionArea[ id=" + id + " ]";
    }
    
}
