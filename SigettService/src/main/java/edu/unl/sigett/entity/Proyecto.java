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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import static javax.persistence.ParameterMode.IN;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@XmlRootElement
@NamedStoredProcedureQueries({

   
    @NamedStoredProcedureQuery(
            name = "proyectoByPeriodo",
            resultClasses = Proyecto.class,
            procedureName = "stored_procedure_proyectos_by_periodo",
            parameters = {
                @StoredProcedureParameter(mode = IN, name = "per_id", type = Integer.class)
            }
    ),
    @NamedStoredProcedureQuery(
            name = "proyectosPorCulminar",
            resultClasses = Proyecto.class,
            procedureName = "sp_proyectos_por_culminar",
            parameters = {
                @StoredProcedureParameter(mode = IN, name = "carreraId", type = Integer.class)
            }
    ),
    @NamedStoredProcedureQuery(
            name = "proyectosCaducados",
            resultClasses = Proyecto.class,
            procedureName = "sp_proyectos_caducados",
            parameters = {
                @StoredProcedureParameter(mode = IN, name = "carreraId", type = Integer.class)
            }
    )
})
@NamedQueries({
    @NamedQuery(name = "Proyecto.findAll", query = "SELECT p FROM Proyecto p"),
    @NamedQuery(name = "Proyecto.findById", query = "SELECT p FROM Proyecto p WHERE p.id = :id"),
    @NamedQuery(name = "Proyecto.findByDescripcion", query = "SELECT p FROM Proyecto p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "Proyecto.findByFechaCreated", query = "SELECT p FROM Proyecto p WHERE p.fechaCreated = :fechaCreated"),
    @NamedQuery(name = "Proyecto.findByTemaActual", query = "SELECT p FROM Proyecto p WHERE p.temaActual = :temaActual")})
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
    @OneToMany(mappedBy = "proyectoId")
    private List<InformeProyecto> informeProyectoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proyectoId")
    private List<ConfiguracionProyecto> configuracionProyectoList;
    @OneToMany(mappedBy = "proyectoId")
    private List<AutorProyecto> autorProyectoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proyectoId")
    private List<DocumentoProyecto> documentoProyectoList;
    @JoinColumn(name = "tipo_proyecto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TipoProyecto tipoProyectoId;
    @JoinColumn(name = "estado_proyecto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EstadoProyecto estadoProyectoId;
    @JoinColumn(name = "catalogo_proyecto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CatalogoProyecto catalogoProyectoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proyectoId")
    private List<DocenteProyecto> docenteProyectoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proyectoId")
    private List<ProyectoCarreraOferta> proyectoCarreraOfertaList;

    public Proyecto() {
        this.temaProyectoList = new ArrayList<>();
        this.documentoProyectoList = new ArrayList<>();
        this.cronograma = new Cronograma();
        this.configuracionProyectoList = new ArrayList<>();
        this.autorProyectoList = new ArrayList<>();
        this.docenteProyectoList = new ArrayList<>();
        this.documentoProyectoList = new ArrayList<>();
        this.lineaInvestigacionProyectoList = new ArrayList<>();
        this.directorProyectoList = new ArrayList<>();
        this.informeProyectoList = new ArrayList<>();
        this.proyectoCarreraOfertaList = new ArrayList<>();
        this.tribunalList = new ArrayList<>();
    }

    public Proyecto(Long id) {
        this.id = id;
    }

    public Proyecto(Long id, String descripcion, Date fechaCreated, String temaActual) {
        this.id = id;
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
    public List<InformeProyecto> getInformeProyectoList() {
        return informeProyectoList;
    }

    public void setInformeProyectoList(List<InformeProyecto> informeProyectoList) {
        this.informeProyectoList = informeProyectoList;
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

    public TipoProyecto getTipoProyectoId() {
        return tipoProyectoId;
    }

    public void setTipoProyectoId(TipoProyecto tipoProyectoId) {
        this.tipoProyectoId = tipoProyectoId;
    }

    public EstadoProyecto getEstadoProyectoId() {
        return estadoProyectoId;
    }

    public void setEstadoProyectoId(EstadoProyecto estadoProyectoId) {
        this.estadoProyectoId = estadoProyectoId;
    }

    public CatalogoProyecto getCatalogoProyectoId() {
        return catalogoProyectoId;
    }

    public void setCatalogoProyectoId(CatalogoProyecto catalogoProyectoId) {
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
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unl.sigett.entity.Proyecto[ id=" + id + " ]";
    }

}
