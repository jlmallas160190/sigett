/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.entity;

import java.io.Serializable;
import java.util.ArrayList;
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
@Table(name = "catalogo", schema = "comun")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Catalogo.findAll", query = "SELECT c FROM Catalogo c"),
    @NamedQuery(name = "Catalogo.findById", query = "SELECT c FROM Catalogo c WHERE c.id = :id"),
    @NamedQuery(name = "Catalogo.findByNombre", query = "SELECT c FROM Catalogo c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Catalogo.findByCodigo", query = "SELECT c FROM Catalogo c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "Catalogo.findByEsActivo", query = "SELECT c FROM Catalogo c WHERE c.esActivo = :esActivo"),
    @NamedQuery(name = "Catalogo.findByIdPadre", query = "SELECT c FROM Catalogo c WHERE c.idPadre = :idPadre")})
public class Catalogo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_activo")
    private Boolean esActivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_padre")
    private Long idPadre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "catalogoId")
    private List<Item> itemList;

    public Catalogo() {
        this.itemList = new ArrayList<>();
    }

    public Catalogo(Long id) {
        this.id = id;
    }

    public Catalogo(Long id, String nombre, String codigo, Boolean esActivo, Long idPadre) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.esActivo = esActivo;
        this.idPadre = idPadre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean isEsActivo() {
        return esActivo;
    }

    public void setEsActivo(Boolean esActivo) {
        this.esActivo = esActivo;
    }

    public Long getIdPadre() {
        return idPadre;
    }

    public void setIdPadre(Long idPadre) {
        this.idPadre = idPadre;
    }

    @XmlTransient
    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
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
        if (!(object instanceof Catalogo)) {
            return false;
        }
        Catalogo other = (Catalogo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jlmallas.comun.entity.Catalogo[ id=" + id + " ]";
    }

}
