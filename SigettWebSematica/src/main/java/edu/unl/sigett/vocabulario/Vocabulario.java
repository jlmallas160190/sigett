/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.vocabulario;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.XSD;

/**
 *
 * @author jorge-luis
 */
public class Vocabulario {
    
    private OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
    private String NS = "http://www.owl-ontologies.com/sigett/";
    
    private OntClass proyecto;
    private OntClass autor;
    private OntClass autorProyecto;
    private OntClass investigador;
    private OntClass docente;
    private OntClass directorProyecto;
    private OntClass lineaInvestigacion;
    private OntClass lineaInvestigacionProyecto;
    private OntClass carrera;
    private OntClass periodoAcademico;
    private OntClass ofertaAcademica;
    private OntClass proyectoOfertaCarrera;
    private OntClass area;
    private OntClass nivelAcademico;
    private OntClass estadoProyecto;
    
    private DatatypeProperty id;
    private DatatypeProperty numeroIdentificacion;
    private DatatypeProperty hasAutor;
    private DatatypeProperty hasProyecto;
    private DatatypeProperty hasDocente;
    private DatatypeProperty esParteDeArea;
    private DatatypeProperty sigla;
    private DatatypeProperty esParteDeNivelAcademico;
    private DatatypeProperty esParteDePeriodoAcademico;
    private DatatypeProperty fechaInicioAcademico;
    private DatatypeProperty fechaPublicacion;
    private DatatypeProperty fechaFinAcademico;
    private DatatypeProperty hasOfertaAcademica;
    private DatatypeProperty hasCarrera;
    private DatatypeProperty hasLineaInvestigacion;
    private DatatypeProperty esInvestigador;
    private DatatypeProperty hasEstadoProyecto;

    //<editor-fold defaultstate="collapsed" desc="DEFINICIÓN DE RECURSOS">
    public void crearClaseProyecto() {
        this.proyecto = model.createClass(getNS() + "proyecto");
    }
    
    public OntClass editarClaseProyecto() {
        this.proyecto = model.getOntClass(getNS() + "proyecto");
        return this.proyecto;
    }
    
    public void crearClaseNivelAcademico() {
        this.nivelAcademico = model.createClass(getNS() + "nivelAcademico");
    }
    
    public OntClass editarClaseNivelAcademico() {
        this.nivelAcademico = model.getOntClass(getNS() + "nivelAcademico");
        return this.nivelAcademico;
    }
    
    public void crearClaseEstadoProyecto() {
        this.estadoProyecto = model.createClass(getNS() + "estadoProyecto");
    }
    
    public OntClass editarClaseEstadoProyecto() {
        this.estadoProyecto = model.getOntClass(getNS() + "estadoProyecto");
        return this.estadoProyecto;
    }
    
    public void crearClaseCarrera() {
        this.carrera = model.createClass(getNS() + "carrera");
    }
    
    public OntClass editarClaseCarrera() {
        this.carrera = model.getOntClass(getNS() + "carrera");
        return this.carrera;
    }
    
    public void crearClaseOfertaAcademica() {
        this.ofertaAcademica = model.createClass(getNS() + "ofertaAcademica");
    }
    
    public OntClass editarClaseOfertaAcademica() {
        this.ofertaAcademica = model.getOntClass(getNS() + "ofertaAcademica");
        return this.ofertaAcademica;
    }
    
    public void crearClaseArea() {
        this.area = model.createClass(getNS() + "area");
    }
    
    public OntClass editarClaseArea() {
        this.area = model.getOntClass(getNS() + "area");
        return this.area;
    }
    
    public void crearClasePeriodoAcademico() {
        this.periodoAcademico = model.createClass(getNS() + "periodoAcademico");
    }
    
    public OntClass editarClasePeriodoAcademico() {
        this.periodoAcademico = model.getOntClass(getNS() + "periodoAcademico");
        return this.periodoAcademico;
    }
    
    public void crearClaseProyectoCarreraOferta() {
        this.proyectoOfertaCarrera = model.createClass(getNS() + "proyectoCarreraOferta");
    }
    
    public OntClass editarClaseProyectoCarreraOferta() {
        this.proyectoOfertaCarrera = model.getOntClass(getNS() + "proyectoCarreraOferta");
        return this.proyectoOfertaCarrera;
    }
    
    public void crearClaseLineaInvestigacion() {
        this.lineaInvestigacion = model.createClass(getNS() + "lineaInvestigacion");
    }
    
    public OntClass editarClaseLineaInvestigacion() {
        this.lineaInvestigacion = model.getOntClass(getNS() + "lineaInvestigacion");
        return this.lineaInvestigacion;
    }
    
    public void crearClaseLineaInvestigacionProyecto() {
        this.lineaInvestigacionProyecto = model.createClass(getNS() + "lineaInvestigacionProyecto");
    }
    
    public OntClass editarClaseLineaInvestigacionProyecto() {
        this.lineaInvestigacionProyecto = model.getOntClass(getNS() + "lineaInvestigacionProyecto");
        return this.lineaInvestigacionProyecto;
    }
    
    public void crearClaseInvestigador() {
        this.investigador = model.createClass(getNS() + "investigador");
    }
    
    public OntClass editarClaseInvestigador() {
        this.investigador = model.getOntClass(getNS() + "investigador");
        return this.investigador;
    }
    
    public void crearClaseAutor() {
        this.autor = model.createClass(getNS() + "autor");
    }
    
    public OntClass editarClaseAutor() {
        this.autor = model.getOntClass(getNS() + "autor");
        return this.autor;
    }
    
    public void crearClaseDocente() {
        this.docente = model.createClass(getNS() + "docente");
    }
    
    public OntClass editarClaseDocente() {
        this.docente = model.getOntClass(getNS() + "docente");
        return this.docente;
    }
    
    public void crearClaseAutorProyecto() {
        this.autorProyecto = model.createClass(getNS() + "autorProyecto");
    }
    
    public OntClass editarClaseAutorProyecto() {
        this.autorProyecto = model.getOntClass(getNS() + "autorProyecto");
        return this.autorProyecto;
    }
    
    public void crearClaseDirectorProyecto() {
        this.directorProyecto = model.createClass(getNS() + "directorProyecto");
    }
    
    public OntClass editarClaseDirectorProyecto() {
        this.directorProyecto = model.getOntClass(getNS() + "directorProyecto");
        return this.directorProyecto;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="DEFINICIÓN DE PROPIEDADES">

    public void crearPropiedadId() {
        this.id = model.createDatatypeProperty(getNS() + "id");
        this.id.addDomain(proyecto);
        this.id.addDomain(autor);
        this.id.addDomain(docente);
        this.id.addDomain(directorProyecto);
        this.id.addDomain(autorProyecto);
        this.id.addDomain(area);
        this.id.addDomain(carrera);
        this.id.addDomain(ofertaAcademica);
        this.id.addDomain(periodoAcademico);
        this.id.addDomain(proyectoOfertaCarrera);
        this.id.addDomain(lineaInvestigacion);
        this.id.addDomain(lineaInvestigacionProyecto);
        this.id.addDomain(nivelAcademico);
        this.id.addDomain(estadoProyecto);
        this.id.addRange(XSD.xlong);
        this.id.convertToFunctionalProperty();
    }
    
    public DatatypeProperty editarPropiedadId() {
        this.id = model.getDatatypeProperty(getNS() + "id");
        return this.id;
    }
    
    public void crearPropiedadNumeroIdentificacion() {
        this.numeroIdentificacion = model.createDatatypeProperty(getNS() + "numeroIdentificacion");
        this.numeroIdentificacion.addDomain(investigador);
        this.numeroIdentificacion.addRange(XSD.xstring);
        this.numeroIdentificacion.convertToFunctionalProperty();
    }
    
    public DatatypeProperty editarPropiedadNumeroIdentificacion() {
        this.numeroIdentificacion = model.getDatatypeProperty(getNS() + "numeroIdentificacion");
        return this.numeroIdentificacion;
    }
    
    public void crearPropiedadHasAutor() {
        this.hasAutor = model.createDatatypeProperty(getNS() + "hasAutor");
        this.hasAutor.addDomain(autorProyecto);
        this.hasAutor.addRange(autor);
        this.hasAutor.convertToFunctionalProperty();
    }
    
    public DatatypeProperty editarPropiedadHasAutor() {
        this.hasAutor = model.getDatatypeProperty(getNS() + "hasAutor");
        return this.hasAutor;
    }
    
    public void crearPropiedadHasEstadoProyecto() {
        this.hasEstadoProyecto = model.createDatatypeProperty(getNS() + "hasEstadoProyecto");
        this.hasEstadoProyecto.addDomain(proyecto);
        this.hasEstadoProyecto.addRange(estadoProyecto);
        this.hasEstadoProyecto.convertToFunctionalProperty();
    }
    
    public DatatypeProperty editarPropiedadHasEstadoProyecto() {
        this.hasEstadoProyecto = model.getDatatypeProperty(getNS() + "hasEstadoProyecto");
        return this.hasEstadoProyecto;
    }
    
    public void crearPropiedadHasDocente() {
        this.hasDocente = model.createDatatypeProperty(getNS() + "hasDocente");
        this.hasDocente.addDomain(directorProyecto);
        this.hasDocente.addRange(docente);
        this.hasDocente.convertToFunctionalProperty();
    }
    
    public DatatypeProperty editarPropiedadHasDocente() {
        this.hasDocente = model.getDatatypeProperty(getNS() + "hasDocente");
        return this.hasDocente;
    }
    
    public void crearPropiedadEsParteDeArea() {
        this.esParteDeArea = model.createDatatypeProperty(getNS() + "esParteDeArea");
        this.esParteDeArea.addDomain(carrera);
        this.esParteDeArea.addRange(area);
        this.esParteDeArea.convertToFunctionalProperty();
    }
    
    public DatatypeProperty editarPropiedadEsParteDeArea() {
        this.esParteDeArea = model.getDatatypeProperty(getNS() + "esParteDeArea");
        return this.esParteDeArea;
    }
    
    public void crearPropiedadEsParteDeNivelAcademico() {
        this.esParteDeNivelAcademico = model.createDatatypeProperty(getNS() + "esParteDeNivelAcademico");
        this.esParteDeNivelAcademico.addDomain(carrera);
        this.esParteDeNivelAcademico.addRange(nivelAcademico);
        this.esParteDeNivelAcademico.convertToFunctionalProperty();
    }
    
    public DatatypeProperty editarPropiedadEsParteDeNivelAcademico() {
        this.esParteDeNivelAcademico = model.getDatatypeProperty(getNS() + "esParteDeNivelAcademico");
        return this.esParteDeNivelAcademico;
    }
    
    public void crearPropiedadFechaPublicacion() {
        this.fechaPublicacion = model.createDatatypeProperty(getNS() + "fechaPublicacion");
        this.fechaPublicacion.addDomain(proyecto);
        this.fechaPublicacion.convertToFunctionalProperty();
    }
    
    public DatatypeProperty editarPropiedadFechaPublicacion() {
        this.fechaPublicacion = model.getDatatypeProperty(getNS() + "fechaPublicacion");
        return this.fechaPublicacion;
    }
    
    public void crearPropiedadHasCarrera() {
        this.hasCarrera = model.createDatatypeProperty(getNS() + "hasCarrera");
        this.hasCarrera.addDomain(proyectoOfertaCarrera);
        this.hasCarrera.addRange(carrera);
        this.hasCarrera.convertToFunctionalProperty();
    }
    
    public DatatypeProperty editarPropiedadHasCarrera() {
        this.hasCarrera = model.getDatatypeProperty(getNS() + "hasCarrera");
        return this.hasCarrera;
    }
    
    public void crearPropiedadHasOfertaAcademica() {
        this.hasOfertaAcademica = model.createDatatypeProperty(getNS() + "hasOfertaAcademica");
        this.hasOfertaAcademica.addDomain(proyectoOfertaCarrera);
        this.hasOfertaAcademica.addRange(ofertaAcademica);
        this.hasOfertaAcademica.convertToFunctionalProperty();
    }
    
    public DatatypeProperty editarPropiedadHasOfertaAcademica() {
        this.hasOfertaAcademica = model.getDatatypeProperty(getNS() + "hasOfertaAcademica");
        return this.hasOfertaAcademica;
    }
    
    public void crearPropiedadHasLineaInvestigacion() {
        this.hasLineaInvestigacion = model.createDatatypeProperty(getNS() + "hasLineaInvestigacion");
        this.hasLineaInvestigacion.addDomain(lineaInvestigacionProyecto);
        this.hasLineaInvestigacion.addRange(lineaInvestigacion);
        this.hasLineaInvestigacion.convertToFunctionalProperty();
    }
    
    public DatatypeProperty editarPropiedadHasLineaInvestigacion() {
        this.hasLineaInvestigacion = model.getDatatypeProperty(getNS() + "hasLineaInvestigacion");
        return this.hasLineaInvestigacion;
    }
    
    public void crearPropiedadSigla() {
        this.sigla = model.createDatatypeProperty(getNS() + "sigla");
        this.sigla.addDomain(carrera);
        this.sigla.addRange(XSD.xstring);
        this.sigla.convertToFunctionalProperty();
    }
    
    public DatatypeProperty editarPropiedadSigla() {
        this.sigla = model.getDatatypeProperty(getNS() + "sigla");
        return this.sigla;
    }
    
    public void crearPropiedadFechaInicioAcademico() {
        this.fechaInicioAcademico = model.createDatatypeProperty(getNS() + "fechaInicio");
        this.fechaInicioAcademico.addDomain(ofertaAcademica);
        this.fechaInicioAcademico.addDomain(periodoAcademico);
        this.fechaInicioAcademico.addRange(XSD.xstring);
        this.fechaInicioAcademico.convertToFunctionalProperty();
    }
    
    public DatatypeProperty editarPropiedadFechaInicioAcademico() {
        this.fechaInicioAcademico = model.getDatatypeProperty(getNS() + "fechaInicio");
        return this.fechaInicioAcademico;
    }
    
    public void crearPropiedadFechaFinAcademico() {
        this.fechaFinAcademico = model.createDatatypeProperty(getNS() + "fechaFin");
        this.fechaFinAcademico.addDomain(ofertaAcademica);
        this.fechaFinAcademico.addDomain(periodoAcademico);
        this.fechaFinAcademico.addRange(XSD.xstring);
        this.fechaFinAcademico.convertToFunctionalProperty();
    }
    
    public DatatypeProperty editarPropiedadFechaFinAcademico() {
        this.fechaFinAcademico = model.getDatatypeProperty(getNS() + "fechaFin");
        return this.fechaFinAcademico;
    }
    
    public void crearPropiedadEsParteDePeriodoAcademico() {
        this.esParteDePeriodoAcademico = model.createDatatypeProperty(getNS() + "hasPeriodoAcademico");
        this.esParteDePeriodoAcademico.addDomain(ofertaAcademica);
        this.esParteDePeriodoAcademico.addRange(periodoAcademico);
        this.esParteDePeriodoAcademico.convertToFunctionalProperty();
    }
    
    public DatatypeProperty editarPropiedadEsParteDePeriodoAcademico() {
        this.esParteDePeriodoAcademico = model.getDatatypeProperty(getNS() + "hasPeriodoAcademico");
        return this.esParteDePeriodoAcademico;
    }
    
    public void crearPropiedadHasProyecto() {
        this.hasProyecto = model.createDatatypeProperty(getNS() + "hasProyecto");
        this.hasProyecto.addDomain(autorProyecto);
        this.hasProyecto.addDomain(directorProyecto);
        this.hasProyecto.addDomain(proyectoOfertaCarrera);
        this.hasProyecto.addDomain(lineaInvestigacionProyecto);
        this.hasProyecto.addRange(proyecto);
        this.hasProyecto.convertToFunctionalProperty();
    }
    
    public DatatypeProperty editarPropiedadHasProyecto() {
        this.hasProyecto = model.getDatatypeProperty(getNS() + "hasProyecto");
        return this.hasProyecto;
    }
    
    public void crearPropiedadEsInvestigador() {
        this.esInvestigador = model.createDatatypeProperty(getNS() + "esInvestigador");
        this.esInvestigador.addDomain(autor);
        this.esInvestigador.addRange(docente);
        this.esInvestigador.convertToFunctionalProperty();
    }
    
    public DatatypeProperty editarPropiedadEsInvestigador() {
        this.esInvestigador = model.getDatatypeProperty(getNS() + "esInvestigador");
        return this.esInvestigador;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">

    public OntModel getModel() {
        return model;
    }
    
    public void setModel(OntModel model) {
        this.model = model;
    }
    
    public String getNS() {
        return NS;
    }
    
    public void setNS(String NS) {
        this.NS = NS;
    }
    
    public OntClass getAutor() {
        return autor;
    }
    
    public void setAutor(OntClass autor) {
        this.autor = autor;
    }
    
    public OntClass getProyecto() {
        return proyecto;
    }
    
    public void setProyecto(OntClass proyecto) {
        this.proyecto = proyecto;
    }
    
    public OntClass getAutorProyecto() {
        return autorProyecto;
    }
    
    public void setAutorProyecto(OntClass autorProyecto) {
        this.autorProyecto = autorProyecto;
    }
    
    public OntClass getInvestigador() {
        return investigador;
    }
    
    public void setInvestigador(OntClass investigador) {
        this.investigador = investigador;
    }
    
    public OntClass getDocente() {
        return docente;
    }
    
    public void setDocente(OntClass docente) {
        this.docente = docente;
    }
    
    public OntClass getDirectorProyecto() {
        return directorProyecto;
    }
    
    public void setDirectorProyecto(OntClass directorProyecto) {
        this.directorProyecto = directorProyecto;
    }
    
    public OntClass getLineaInvestigacion() {
        return lineaInvestigacion;
    }
    
    public void setLineaInvestigacion(OntClass lineaInvestigacion) {
        this.lineaInvestigacion = lineaInvestigacion;
    }
    
    public OntClass getLineaInvestigacionProyecto() {
        return lineaInvestigacionProyecto;
    }
    
    public void setLineaInvestigacionProyecto(OntClass lineaInvestigacionProyecto) {
        this.lineaInvestigacionProyecto = lineaInvestigacionProyecto;
    }
    
    public OntClass getCarrera() {
        return carrera;
    }
    
    public void setCarrera(OntClass carrera) {
        this.carrera = carrera;
    }
    
    public OntClass getPeriodoAcademico() {
        return periodoAcademico;
    }
    
    public void setPeriodoAcademico(OntClass periodoAcademico) {
        this.periodoAcademico = periodoAcademico;
    }
    
    public OntClass getOfertaAcademica() {
        return ofertaAcademica;
    }
    
    public void setOfertaAcademica(OntClass ofertaAcademica) {
        this.ofertaAcademica = ofertaAcademica;
    }
    
    public OntClass getProyectoOfertaCarrera() {
        return proyectoOfertaCarrera;
    }
    
    public void setProyectoOfertaCarrera(OntClass proyectoOfertaCarrera) {
        this.proyectoOfertaCarrera = proyectoOfertaCarrera;
    }
    
    public OntClass getArea() {
        return area;
    }
    
    public void setArea(OntClass area) {
        this.area = area;
    }
    
    public OntClass getNivelAcademico() {
        return nivelAcademico;
    }
    
    public void setNivelAcademico(OntClass nivelAcademico) {
        this.nivelAcademico = nivelAcademico;
    }
    
    public OntClass getEstadoProyecto() {
        return estadoProyecto;
    }
    
    public void setEstadoProyecto(OntClass estadoProyecto) {
        this.estadoProyecto = estadoProyecto;
    }
//</editor-fold>

}
