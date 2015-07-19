/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jorge-luis
 */
@Entity
@Table(name = "proyecto")
@Cacheable(value = false)
@XmlRootElement
public class Proyecto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreated;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "tema_actual")
    private String temaActual;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proyectoId")
    private List<DirectorProyecto> directorProyectoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proyectoId")
    private List<Tribunal> tribunalList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "proyecto")
    private Cronograma cronograma;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proyectoId")
    private List<LineaInvestigacionProyecto> lineaInvestigacionProyectoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proyectoId")
    private List<TemaProyecto> temaProyectoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proyectoId")
    private List<ConfiguracionProyecto> configuracionProyectoList;
    @OneToMany(mappedBy = "proyectoId")
    private List<AutorProyecto> autorProyectoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proyectoId")
    private List<DocumentoProyecto> documentoProyectoList;
    @Column(name = "tipo_proyecto_id")
    @NotNull
    private Long tipoProyectoId;
    @Column(name = "estado_proyecto_id")
    @NotNull
    private Long estadoProyectoId;
    @Column(name = "catalogo_proyecto_id")
    @NotNull
    private Long catalogoProyectoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proyectoId")
    private List<DocenteProyecto> docenteProyectoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proyectoId")
    private List<ProyectoCarreraOferta> proyectoCarreraOfertaList;
    @Transient
    private String estado;
    @Transient
    private String tipo;
    @Transient
    private String catalogo;
    @Transient
    private String autores;
    @Transient
    private String directores;
    @Transient
    private String nombreOferta;
    @Transient
    private String carrera;

    public Proyecto() {
        this.tipo = "";
        this.estado = "";
        this.catalogo = "";
        this.temaProyectoList = new ArrayList<>();
        this.documentoProyectoList = new ArrayList<>();
        this.cronograma = new Cronograma();
        this.configuracionProyectoList = new ArrayList<>();
        this.autorProyectoList = new ArrayList<>();
        this.docenteProyectoList = new ArrayList<>();
        this.documentoProyectoList = new ArrayList<>();
        this.lineaInvestigacionProyectoList = new ArrayList<>();
        this.directorProyectoList = new ArrayList<>();
        this.proyectoCarreraOfertaList = new ArrayList<>();
        this.tribunalList = new ArrayList<>();
    }

    public Proyecto(Long id) {
        this.id = id;
    }

    public Proyecto(Long estado, Long catalogo, Long tipo, String descripcion, Date fechaCreated, String temaActual) {
        this.estadoProyectoId = estado;
        this.tipoProyectoId = tipo;
        this.catalogoProyectoId = catalogo;
        this.descripcion = descripcion;
        this.fechaCreated = fechaCreated;
        this.temaActual = temaActual;
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

    public Date getFechaCreated() {
        return fechaCreated;
    }

    public void setFechaCreated(Date fechaCreated) {
        this.fechaCreated = fechaCreated;
    }

    public String getTemaActual() {
        return temaActual;
    }

    public void setTemaActual(String temaActual) {
        this.temaActual = temaActual;
    }

    @XmlTransient
    public List<DirectorProyecto> getDirectorProyectoList() {
        return directorProyectoList;
    }

    public void setDirectorProyectoList(List<DirectorProyecto> directorProyectoList) {
        this.directorProyectoList = directorProyectoList;
    }

    @XmlTransient
    public List<Tribunal> getTribunalList() {
        return tribunalList;
    }

    public void setTribunalList(List<Tribunal> tribunalList) {
        this.tribunalList = tribunalList;
    }

    public Cronograma getCronograma() {
        return cronograma;
    }

    public void setCronograma(Cronograma cronograma) {
        this.cronograma = cronograma;
    }

    @XmlTransient
    public List<LineaInvestigacionProyecto> getLineaInvestigacionProyectoList() {
        return lineaInvestigacionProyectoList;
    }

    public void setLineaInvestigacionProyectoList(List<LineaInvestigacionProyecto> lineaInvestigacionProyectoList) {
        this.lineaInvestigacionProyectoList = lineaInvestigacionProyectoList;
    }

    @XmlTransient
    public List<TemaProyecto> getTemaProyectoList() {
        return temaProyectoList;
    }

    public void setTemaProyectoList(List<TemaProyecto> temaProyectoList) {
        this.temaProyectoList = temaProyectoList;
    }

    @XmlTransient
    public List<ConfiguracionProyecto> getConfiguracionProyectoList() {
        return configuracionProyectoList;
    }

    public void setConfiguracionProyectoList(List<ConfiguracionProyecto> configuracionProyectoList) {
        this.configuracionProyectoList = configuracionProyectoList;
    }

    @XmlTransient
    public List<AutorProyecto> getAutorProyectoList() {
        return autorProyectoList;
    }

    public void setAutorProyectoList(List<AutorProyecto> autorProyectoList) {
        this.autorProyectoList = autorProyectoList;
    }

    @XmlTransient
    public List<DocumentoProyecto> getDocumentoProyectoList() {
        return documentoProyectoList;
    }

    public void setDocumentoProyectoList(List<DocumentoProyecto> documentoProyectoList) {
        this.documentoProyectoList = documentoProyectoList;
    }

    public Long getTipoProyectoId() {
        return tipoProyectoId;
    }

    public void setTipoProyectoId(Long tipoProyectoId) {
        this.tipoProyectoId = tipoProyectoId;
    }

    public Long getEstadoProyectoId() {
        return estadoProyectoId;
    }

    public void setEstadoProyectoId(Long estadoProyectoId) {
        this.estadoProyectoId = estadoProyectoId;
    }

    public Long getCatalogoProyectoId() {
        return catalogoProyectoId;
    }

    public void setCatalogoProyectoId(Long catalogoProyectoId) {
        this.catalogoProyectoId = catalogoProyectoId;
    }

    @XmlTransient
    public List<DocenteProyecto> getDocenteProyectoList() {
        return docenteProyectoList;
    }

    public void setDocenteProyectoList(List<DocenteProyecto> docenteProyectoList) {
        this.docenteProyectoList = docenteProyectoList;
    }

    @XmlTransient
    public List<ProyectoCarreraOferta> getProyectoCarreraOfertaList() {
        return proyectoCarreraOfertaList;
    }

    public void setProyectoCarreraOfertaList(List<ProyectoCarreraOferta> proyectoCarreraOfertaList) {
        this.proyectoCarreraOfertaList = proyectoCarreraOfertaList;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(String catalogo) {
        this.catalogo = catalogo;
    }

    public String getAutores() {
        return autores;
    }

    public void setAutores(String autores) {
        this.autores = autores;
    }

    public String getDirectores() {
        return directores;
    }

    public void setDirectores(String directores) {
        this.directores = directores;
    }

    public String getNombreOferta() {
        return nombreOferta;
    }

    public void setNombreOferta(String nombreOferta) {
        this.nombreOferta = nombreOferta;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
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
        if (!(object instanceof Proyecto)) {
            return false;
        }
        Proyecto other = (Proyecto) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.Proyecto[ id=" + id + " ]";
    }

}
