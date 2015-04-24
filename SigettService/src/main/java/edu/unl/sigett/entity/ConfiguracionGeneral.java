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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "configuracion_general")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConfiguracionGeneral.findAll", query = "SELECT c FROM ConfiguracionGeneral c"),
    @NamedQuery(name = "ConfiguracionGeneral.findById", query = "SELECT c FROM ConfiguracionGeneral c WHERE c.id = :id"),
    @NamedQuery(name = "ConfiguracionGeneral.findByNombre", query = "SELECT c FROM ConfiguracionGeneral c WHERE LOWER(c.nombre) like concat('%',LOWER(:nombre),'%') order by c.nombre"),
    @NamedQuery(name = "ConfiguracionGeneral.findByValor", query = "SELECT c FROM ConfiguracionGeneral c WHERE c.valor = :valor")})

public class ConfiguracionGeneral implements Serializable {

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
    @Size(min = 1, max = 250)
    @Column(name = "valor")
    private String valor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 100)
    @Column(name = "tipo")
    private String tipo;
    @Transient
    private boolean radio;

    public ConfiguracionGeneral() {
    }

    public ConfiguracionGeneral(Integer id) {
        this.id = id;
    }

    public ConfiguracionGeneral(Integer id, String nombre, String valor, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.valor = valor;
        this.descripcion = descripcion;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isRadio() {
        return radio;
    }

    public void setRadio(boolean radio) {
        this.radio = radio;
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
        if (!(object instanceof ConfiguracionGeneral)) {
            return false;
        }
        ConfiguracionGeneral other = (ConfiguracionGeneral) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.ConfiguracionGeneral[ id=" + id + " ]";
    }

}
