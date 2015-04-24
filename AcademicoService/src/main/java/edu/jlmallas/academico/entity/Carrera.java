/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author JorgeLuis
 */
@Entity
@Table(name = "carrera", schema = "academico")
@Cacheable(value = false)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Carrera.findAll", query = "SELECT c FROM Carrera c"),
    @NamedQuery(name = "Carrera.findById", query = "SELECT c FROM Carrera c WHERE c.id = :id"),
    @NamedQuery(name = "Carrera.findByNombre", query = "SELECT c FROM Carrera c WHERE c.nombre = :nombre")})
public class Carrera implements Serializable {

    @Lob
    @Column(name = "logo")
    private byte[] logo;
    @Basic(optional = false)
    @Size(min = 1, max = 250)
    @Column(name = "lugar")
    private String lugar;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "carreraId")
    private List<PeriodoCoordinacion> periodoCoordinacionList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "id_sga")
    @Size(max = 15)
    private String idSga;
    @Basic(optional = false)
    @Column(name = "modalidad")
    @Size(max = 250)
    private String modalidad;
    @NotNull
    @Size(max = 250)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 25)
    @Column(name = "sigla")
    private String sigla;
    @Size(max = 500)
    @Column(name = "nombre_titulo")
    private String nombreTitulo;
    @JoinColumn(name = "nivel_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Nivel nivelId;
    @JoinColumn(name = "area_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Area areaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "carreraId")
    private List<EstudianteCarrera> estudianteCarreraList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "carreraId")
    private List<DocenteCarrera> docenteCarreraList;
    @Transient
    private boolean esEditado;

    public Carrera() {
        lugar = "S/N";
        this.modalidad = "";
        this.nombreTitulo = "";
        esEditado = false;
    }

    public Carrera(Integer id) {
        this.id = id;
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

    public String getIdSga() {
        return idSga;
    }

    public void setIdSga(String idSga) {
        this.idSga = idSga;
    }

    public Nivel getNivelId() {
        return nivelId;
    }

    public void setNivelId(Nivel nivelId) {
        this.nivelId = nivelId;
    }

    public Area getAreaId() {
        return areaId;
    }

    public void setAreaId(Area areaId) {
        this.areaId = areaId;
    }

    public boolean isEsEditado() {
        return esEditado;
    }

    public void setEsEditado(boolean esEditado) {
        this.esEditado = esEditado;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
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
        if (!(object instanceof Carrera)) {
            return false;
        }
        Carrera other = (Carrera) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id + ": " + nombre;
    }

    @XmlTransient
    public List<EstudianteCarrera> getEstudianteCarreraList() {
        return estudianteCarreraList;
    }

    public void setEstudianteCarreraList(List<EstudianteCarrera> estudianteCarreraList) {
        this.estudianteCarreraList = estudianteCarreraList;
    }

    @XmlTransient
    public List<DocenteCarrera> getDocenteCarreraList() {
        return docenteCarreraList;
    }

    public void setDocenteCarreraList(List<DocenteCarrera> docenteCarreraList) {
        this.docenteCarreraList = docenteCarreraList;
    }

    @XmlTransient
    public List<PeriodoCoordinacion> getPeriodoCoordinacionList() {
        return periodoCoordinacionList;
    }

    public void setPeriodoCoordinacionList(List<PeriodoCoordinacion> periodoCoordinacionList) {
        this.periodoCoordinacionList = periodoCoordinacionList;
    }

    public String getNombreTitulo() {
        return nombreTitulo;
    }

    public void setNombreTitulo(String nombreTitulo) {
        this.nombreTitulo = nombreTitulo;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

}
