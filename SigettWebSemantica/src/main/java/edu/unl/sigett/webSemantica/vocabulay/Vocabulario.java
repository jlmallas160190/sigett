/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.webSemantica.vocabulay;

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
    //<editor-fold defaultstate="collapsed" desc="DECLARACIÓN DE RECURSOS">
    private OntClass proyectoOnt;
    private OntClass autorOnt;
    private OntClass autorProyectoOnt;
    private OntClass docenteOnt;
    private OntClass directorProyectoOnt;
    private OntClass lineaInvestigacionOnt;
    private OntClass lineaInvestigacionProyectoOnt;
    private OntClass carreraOnt;
    private OntClass periodoAcademicoOnt;
    private OntClass ofertaAcademicaOnt;
    private OntClass proyectoOfertaCarreraOnt;
    private OntClass areaAcademicaOnt;
    private OntClass nivelAcademicoOnt;
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="DEFINICIÓN DE PROPIEDADES">
    private DatatypeProperty id;
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
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="METODOS DE RECURSOS">

    public void crearProyectoOnt() {
        this.proyectoOnt = model.createClass(getNS() + "proyecto");
    }

    public OntClass editarProyectoOnt() {
        this.proyectoOnt = model.getOntClass(getNS() + "proyecto");
        return this.proyectoOnt;
    }

    public void crearNivelAcademicoOnt() {
        this.nivelAcademicoOnt = model.createClass(getNS() + "nivelAcademico");
    }

    public OntClass editarNivelAcademicoOnt() {
        this.nivelAcademicoOnt = model.getOntClass(getNS() + "nivelAcademico");
        return this.nivelAcademicoOnt;
    }

    public void crearCarreraOnt() {
        this.carreraOnt = model.createClass(getNS() + "carrera");
    }

    public OntClass editarCarreraOnt() {
        this.carreraOnt = model.getOntClass(getNS() + "carrera");
        return this.carreraOnt;
    }

    public void crearOfertaAcademicaOnt() {
        this.ofertaAcademicaOnt = model.createClass(getNS() + "ofertaAcademica");
    }

    public OntClass editarClaseOfertaAcademica() {
        this.ofertaAcademicaOnt = model.getOntClass(getNS() + "ofertaAcademica");
        return this.ofertaAcademicaOnt;
    }

    public void crearAreaOnt() {
        this.areaAcademicaOnt = model.createClass(getNS() + "area");
    }

    public OntClass editarAreaOnt() {
        this.areaAcademicaOnt = model.getOntClass(getNS() + "area");
        return this.areaAcademicaOnt;
    }

    public void crearPeriodoAcademicoOnt() {
        this.periodoAcademicoOnt = model.createClass(getNS() + "periodoAcademico");
    }

    public OntClass editarPeriodoAcademicoOnt() {
        this.periodoAcademicoOnt = model.getOntClass(getNS() + "periodoAcademico");
        return this.periodoAcademicoOnt;
    }

    public void crearProyectoCarreraOfertaOnt() {
        this.proyectoOfertaCarreraOnt = model.createClass(getNS() + "proyectoCarreraOferta");
    }

    public OntClass editarProyectoCarreraOfertaOnt() {
        this.proyectoOfertaCarreraOnt = model.getOntClass(getNS() + "proyectoCarreraOferta");
        return this.proyectoOfertaCarreraOnt;
    }

    public void crearLineaInvestigacionOnt() {
        this.lineaInvestigacionOnt = model.createClass(getNS() + "lineaInvestigacion");
    }

    public OntClass editarLineaInvestigacionOnt() {
        this.lineaInvestigacionOnt = model.getOntClass(getNS() + "lineaInvestigacion");
        return this.lineaInvestigacionOnt;
    }

    public void crearLineaInvestigacionProyectoOnt() {
        this.lineaInvestigacionProyectoOnt = model.createClass(getNS() + "lineaInvestigacionProyecto");
    }

    public OntClass editarLineaInvestigacionProyectoOnt() {
        this.lineaInvestigacionProyectoOnt = model.getOntClass(getNS() + "lineaInvestigacionProyecto");
        return this.lineaInvestigacionProyectoOnt;
    }

    public void crearAutorOnt() {
        this.autorOnt = model.createClass(getNS() + "autor");
    }

    public OntClass editarAutorOnt() {
        this.autorOnt = model.getOntClass(getNS() + "autor");
        return this.autorOnt;
    }

    public void crearDocenteOnt() {
        this.docenteOnt = model.createClass(getNS() + "docente");
    }

    public OntClass editarDocenteOnt() {
        this.docenteOnt = model.getOntClass(getNS() + "docente");
        return this.docenteOnt;
    }

    public void crearAutorProyectoOnt() {
        this.autorProyectoOnt = model.createClass(getNS() + "autorProyecto");
    }

    public OntClass editarAutorProyectoOnt() {
        this.autorProyectoOnt = model.getOntClass(getNS() + "autorProyecto");
        return this.autorProyectoOnt;
    }

    public void crearDirectorProyectoOnt() {
        this.directorProyectoOnt = model.createClass(getNS() + "directorProyecto");
    }

    public OntClass editarDirectorProyectoOnt() {
        this.directorProyectoOnt = model.getOntClass(getNS() + "directorProyecto");
        return this.directorProyectoOnt;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="METODOS DE PROPIEDADES">

    public void crearPropiedadId() {
        this.id = model.createDatatypeProperty(getNS() + "id");
        this.id.addDomain(proyectoOnt);
        this.id.addDomain(autorOnt);
        this.id.addDomain(docenteOnt);
        this.id.addDomain(directorProyectoOnt);
        this.id.addDomain(autorProyectoOnt);
        this.id.addDomain(areaAcademicaOnt);
        this.id.addDomain(carreraOnt);
        this.id.addDomain(ofertaAcademicaOnt);
        this.id.addDomain(periodoAcademicoOnt);
        this.id.addDomain(proyectoOfertaCarreraOnt);
        this.id.addDomain(lineaInvestigacionOnt);
        this.id.addDomain(lineaInvestigacionProyectoOnt);
        this.id.addDomain(nivelAcademicoOnt);
        this.id.addRange(XSD.xlong);
        this.id.convertToFunctionalProperty();
    }

    public DatatypeProperty editarPropiedadId() {
        this.id = model.getDatatypeProperty(getNS() + "id");
        return this.id;
    }

    public void crearPropiedadHasAutor() {
        this.hasAutor = model.createDatatypeProperty(getNS() + "hasAutor");
        this.hasAutor.addDomain(autorProyectoOnt);
        this.hasAutor.addRange(autorOnt);
        this.hasAutor.convertToFunctionalProperty();
    }

    public DatatypeProperty editarPropiedadHasAutor() {
        this.hasAutor = model.getDatatypeProperty(getNS() + "hasAutor");
        return this.hasAutor;
    }

    public void crearPropiedadHasDocente() {
        this.hasDocente = model.createDatatypeProperty(getNS() + "hasDocente");
        this.hasDocente.addDomain(directorProyectoOnt);
        this.hasDocente.addRange(docenteOnt);
        this.hasDocente.convertToFunctionalProperty();
    }

    public DatatypeProperty editarPropiedadHasDocente() {
        this.hasDocente = model.getDatatypeProperty(getNS() + "hasDocente");
        return this.hasDocente;
    }

    public void crearPropiedadEsParteDeArea() {
        this.esParteDeArea = model.createDatatypeProperty(getNS() + "esParteDeArea");
        this.esParteDeArea.addDomain(carreraOnt);
        this.esParteDeArea.addRange(areaAcademicaOnt);
        this.esParteDeArea.convertToFunctionalProperty();
    }

    public DatatypeProperty editarPropiedadEsParteDeArea() {
        this.esParteDeArea = model.getDatatypeProperty(getNS() + "esParteDeArea");
        return this.esParteDeArea;
    }

    public void crearPropiedadEsParteDeNivelAcademico() {
        this.esParteDeNivelAcademico = model.createDatatypeProperty(getNS() + "esParteDeNivelAcademico");
        this.esParteDeNivelAcademico.addDomain(carreraOnt);
        this.esParteDeNivelAcademico.addRange(nivelAcademicoOnt);
        this.esParteDeNivelAcademico.convertToFunctionalProperty();
    }

    public DatatypeProperty editarPropiedadEsParteDeNivelAcademico() {
        this.esParteDeNivelAcademico = model.getDatatypeProperty(getNS() + "esParteDeNivelAcademico");
        return this.esParteDeNivelAcademico;
    }

    public void crearPropiedadFechaPublicacion() {
        this.fechaPublicacion = model.createDatatypeProperty(getNS() + "fechaPublicacion");
        this.fechaPublicacion.addDomain(proyectoOnt);
        this.fechaPublicacion.convertToFunctionalProperty();
    }

    public DatatypeProperty editarPropiedadFechaPublicacion() {
        this.fechaPublicacion = model.getDatatypeProperty(getNS() + "fechaPublicacion");
        return this.fechaPublicacion;
    }

    public void crearPropiedadHasCarrera() {
        this.hasCarrera = model.createDatatypeProperty(getNS() + "hasCarrera");
        this.hasCarrera.addDomain(proyectoOfertaCarreraOnt);
        this.hasCarrera.addRange(carreraOnt);
        this.hasCarrera.convertToFunctionalProperty();
    }

    public DatatypeProperty editarPropiedadHasCarrera() {
        this.hasCarrera = model.getDatatypeProperty(getNS() + "hasCarrera");
        return this.hasCarrera;
    }

    public void crearPropiedadHasOfertaAcademica() {
        this.hasOfertaAcademica = model.createDatatypeProperty(getNS() + "hasOfertaAcademica");
        this.hasOfertaAcademica.addDomain(proyectoOfertaCarreraOnt);
        this.hasOfertaAcademica.addRange(ofertaAcademicaOnt);
        this.hasOfertaAcademica.convertToFunctionalProperty();
    }

    public DatatypeProperty editarPropiedadHasOfertaAcademica() {
        this.hasOfertaAcademica = model.getDatatypeProperty(getNS() + "hasOfertaAcademica");
        return this.hasOfertaAcademica;
    }

    public void crearPropiedadHasLineaInvestigacion() {
        this.hasLineaInvestigacion = model.createDatatypeProperty(getNS() + "hasLineaInvestigacion");
        this.hasLineaInvestigacion.addDomain(lineaInvestigacionProyectoOnt);
        this.hasLineaInvestigacion.addRange(lineaInvestigacionOnt);
        this.hasLineaInvestigacion.convertToFunctionalProperty();
    }

    public DatatypeProperty editarPropiedadHasLineaInvestigacion() {
        this.hasLineaInvestigacion = model.getDatatypeProperty(getNS() + "hasLineaInvestigacion");
        return this.hasLineaInvestigacion;
    }

    public void crearPropiedadSigla() {
        this.sigla = model.createDatatypeProperty(getNS() + "sigla");
        this.sigla.addDomain(carreraOnt);
        this.sigla.addRange(XSD.xstring);
        this.sigla.convertToFunctionalProperty();
    }

    public DatatypeProperty editarPropiedadSigla() {
        this.sigla = model.getDatatypeProperty(getNS() + "sigla");
        return this.sigla;
    }

    public void crearPropiedadFechaInicioAcademico() {
        this.fechaInicioAcademico = model.createDatatypeProperty(getNS() + "fechaInicio");
        this.fechaInicioAcademico.addDomain(ofertaAcademicaOnt);
        this.fechaInicioAcademico.addDomain(periodoAcademicoOnt);
        this.fechaInicioAcademico.addRange(XSD.xstring);
        this.fechaInicioAcademico.convertToFunctionalProperty();
    }

    public DatatypeProperty editarPropiedadFechaInicioAcademico() {
        this.fechaInicioAcademico = model.getDatatypeProperty(getNS() + "fechaInicio");
        return this.fechaInicioAcademico;
    }

    public void crearPropiedadFechaFinAcademico() {
        this.fechaFinAcademico = model.createDatatypeProperty(getNS() + "fechaFin");
        this.fechaFinAcademico.addDomain(ofertaAcademicaOnt);
        this.fechaFinAcademico.addDomain(periodoAcademicoOnt);
        this.fechaFinAcademico.addRange(XSD.xstring);
        this.fechaFinAcademico.convertToFunctionalProperty();
    }

    public DatatypeProperty editarPropiedadFechaFinAcademico() {
        this.fechaFinAcademico = model.getDatatypeProperty(getNS() + "fechaFin");
        return this.fechaFinAcademico;
    }

    public void crearPropiedadEsParteDePeriodoAcademico() {
        this.esParteDePeriodoAcademico = model.createDatatypeProperty(getNS() + "hasPeriodoAcademico");
        this.esParteDePeriodoAcademico.addDomain(ofertaAcademicaOnt);
        this.esParteDePeriodoAcademico.addRange(periodoAcademicoOnt);
        this.esParteDePeriodoAcademico.convertToFunctionalProperty();
    }

    public DatatypeProperty editarPropiedadEsParteDePeriodoAcademico() {
        this.esParteDePeriodoAcademico = model.getDatatypeProperty(getNS() + "hasPeriodoAcademico");
        return this.esParteDePeriodoAcademico;
    }
/**
 * PROPIEDAD DONDE LAS ONTOLOGÍAS AUTORPROYECTO, DIRECTORPROYECTO,LINEAINVESTIGACIONPROYECTO Y PROYECTOCARREAOFERTA
 * TIENEN UNA RELACION CON PROYECTO
 */
    public void crearPropiedadHasProyecto() {
        this.hasProyecto = model.createDatatypeProperty(getNS() + "hasProyecto");
        this.hasProyecto.addDomain(autorProyectoOnt);
        this.hasProyecto.addDomain(directorProyectoOnt);
        this.hasProyecto.addDomain(proyectoOfertaCarreraOnt);
        this.hasProyecto.addDomain(lineaInvestigacionProyectoOnt);
        this.hasProyecto.addRange(proyectoOnt);
        this.hasProyecto.convertToFunctionalProperty();
    }

    public DatatypeProperty editarPropiedadHasProyecto() {
        this.hasProyecto = model.getDatatypeProperty(getNS() + "hasProyecto");
        return this.hasProyecto;
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

    public OntClass getAutorOnt() {
        return autorOnt;
    }

    public void setAutorOnt(OntClass autorOnt) {
        this.autorOnt = autorOnt;
    }

    public OntClass getProyectoOnt() {
        return proyectoOnt;
    }

    public void setProyectoOnt(OntClass proyectoOnt) {
        this.proyectoOnt = proyectoOnt;
    }

    public OntClass getAutorProyectoOnt() {
        return autorProyectoOnt;
    }

    public void setAutorProyectoOnt(OntClass autorProyectoOnt) {
        this.autorProyectoOnt = autorProyectoOnt;
    }

    public OntClass getDocenteOnt() {
        return docenteOnt;
    }

    public void setDocenteOnt(OntClass docenteOnt) {
        this.docenteOnt = docenteOnt;
    }

    public OntClass getDirectorProyectoOnt() {
        return directorProyectoOnt;
    }

    public void setDirectorProyectoOnt(OntClass directorProyectoOnt) {
        this.directorProyectoOnt = directorProyectoOnt;
    }

    public OntClass getLineaInvestigacionOnt() {
        return lineaInvestigacionOnt;
    }

    public void setLineaInvestigacionOnt(OntClass lineaInvestigacionOnt) {
        this.lineaInvestigacionOnt = lineaInvestigacionOnt;
    }

    public OntClass getLineaInvestigacionProyectoOnt() {
        return lineaInvestigacionProyectoOnt;
    }

    public void setLineaInvestigacionProyectoOnt(OntClass lineaInvestigacionProyectoOnt) {
        this.lineaInvestigacionProyectoOnt = lineaInvestigacionProyectoOnt;
    }

    public OntClass getCarreraOnt() {
        return carreraOnt;
    }

    public void setCarreraOnt(OntClass carreraOnt) {
        this.carreraOnt = carreraOnt;
    }

    public OntClass getPeriodoAcademicoOnt() {
        return periodoAcademicoOnt;
    }

    public void setPeriodoAcademicoOnt(OntClass periodoAcademicoOnt) {
        this.periodoAcademicoOnt = periodoAcademicoOnt;
    }

    public OntClass getOfertaAcademicaOnt() {
        return ofertaAcademicaOnt;
    }

    public void setOfertaAcademicaOnt(OntClass ofertaAcademicaOnt) {
        this.ofertaAcademicaOnt = ofertaAcademicaOnt;
    }

    public OntClass getProyectoOfertaCarreraOnt() {
        return proyectoOfertaCarreraOnt;
    }

    public void setProyectoOfertaCarreraOnt(OntClass proyectoOfertaCarreraOnt) {
        this.proyectoOfertaCarreraOnt = proyectoOfertaCarreraOnt;
    }

    public OntClass getAreaAcademicaOnt() {
        return areaAcademicaOnt;
    }

    public void setAreaAcademicaOnt(OntClass areaAcademicaOnt) {
        this.areaAcademicaOnt = areaAcademicaOnt;
    }

    public OntClass getNivelAcademicoOnt() {
        return nivelAcademicoOnt;
    }

    public void setNivelAcademicoOnt(OntClass nivelAcademicoOnt) {
        this.nivelAcademicoOnt = nivelAcademicoOnt;
    }
//</editor-fold>

}
