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
@Table(name = "configuracion_carrera")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConfiguracionCarrera.findAll", query = "SELECT c FROM ConfiguracionCarrera c"),
    @NamedQuery(name = "ConfiguracionCarrera.findById", query = "SELECT c FROM ConfiguracionCarrera c WHERE c.id = :id"),
    @NamedQuery(name = "ConfiguracionCarrera.findByNombre", query = "SELECT c FROM ConfiguracionCarrera c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "ConfiguracionCarrera.findByValor", query = "SELECT c FROM ConfiguracionCarrera c WHERE c.valor = :valor"),
    @NamedQuery(name = "ConfiguracionCarrera.findByCarreraId", query = "SELECT c FROM ConfiguracionCarrera c WHERE c.carreraId = :carreraId"),
    @NamedQuery(name = "ConfiguracionCarrera.findByCodigo", query = "SELECT c FROM ConfiguracionCarrera c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "ConfiguracionCarrera.findByObservacion", query = "SELECT c FROM ConfiguracionCarrera c WHERE c.observacion = :observacion"),
    @NamedQuery(name = "ConfiguracionCarrera.findByTipo", query = "SELECT c FROM ConfiguracionCarrera c WHERE c.tipo = :tipo")})
public class ConfiguracionCarrera implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "valor")
    private String valor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "carrera_id")
    private Integer carreraId;
    @Size(max = 50)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "observacion")
    private String observacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "tipo")
    private String tipo;

    public ConfiguracionCarrera() {
    }

    public ConfiguracionCarrera(Integer id) {
        this.id = id;
    }

    public ConfiguracionCarrera(Integer carreraId, String codigo) {
        this.carreraId = carreraId;
        this.codigo = codigo;
    }

    public ConfiguracionCarrera(Integer id, String nombre, String valor, int carreraId, String observacion, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.valor = valor;
        this.carreraId = carreraId;
        this.observacion = observacion;
        this.tipo = tipo;
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

    public Integer getCarreraId() {
        return carreraId;
    }

    public void setCarreraId(Integer carreraId) {
        this.carreraId = carreraId;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
        if (!(object instanceof ConfiguracionCarrera)) {
            return false;
        }
        ConfiguracionCarrera other = (ConfiguracionCarrera) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.ConfiguracionCarrera[ id=" + id + " ]";
    }

}
