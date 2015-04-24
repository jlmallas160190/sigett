/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.entity;

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
@Table(name = "estado_matricula", schema = "academico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoMatricula.findAll", query = "SELECT e FROM EstadoMatricula e"),
    @NamedQuery(name = "EstadoMatricula.findById", query = "SELECT e FROM EstadoMatricula e WHERE e.id = :id"),
    @NamedQuery(name = "EstadoMatricula.findByCodigo", query = "SELECT e FROM EstadoMatricula e WHERE e.codigo = :codigo"),
    @NamedQuery(name = "EstadoMatricula.findByNombre", query = "SELECT e FROM EstadoMatricula e WHERE e.nombre = :nombre")})
public class EstadoMatricula implements Serializable {

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
    @Size(min = 1, max = 100)
    @Column(name = "codigo")
    private String codigo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoMatriculaId")
    private List<ReporteMatricula> reporteMatriculaList;

    public EstadoMatricula() {
    }

    public EstadoMatricula(Integer id) {
        this.id = id;
    }

    public EstadoMatricula(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @XmlTransient
    public List<ReporteMatricula> getReporteMatriculaList() {
        return reporteMatriculaList;
    }

    public void setReporteMatriculaList(List<ReporteMatricula> reporteMatriculaList) {
        this.reporteMatriculaList = reporteMatriculaList;
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
        if (!(object instanceof EstadoMatricula)) {
            return false;
        }
        EstadoMatricula other = (EstadoMatricula) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.jlmallas.academico.entity.EstadoMatricula[ id=" + id + " ]";
    }

}
