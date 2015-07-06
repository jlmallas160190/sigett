/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.util;


import edu.unl.sigett.lud.service.*;
import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class OntologyService implements Serializable {

    private AutorOntService autorOntService;
    private AutorProyectoOntService autorProyectoOntService;
    private ProyectoOntService proyectoOntService;
    private CarreraOntService carreraOntService;
    private OfertaAcademicoOntService ofertaAcademicoOntService;
    private PeriodoAcademicoOntService periodoAcademicoOntService;
    private AreaAcademicaOntService areaAcademicaOntService;
    private NivelAcademicoOntService nivelAcademicoOntService;
    private LineaInvestigacionOntService lineaInvestigacionOntService;
    private LineaInvestigacionProyectoOntService lineaInvestigacionProyectoOntService;
    private ProyectoCarreraOfertaOntService proyectoCarreraOfertaOntService;
    private DocenteOntService docenteOntService;
    private DirectorProyectoOntService directorProyectoOntService;

    public OntologyService() {
    }

    public AutorOntService getAutorOntService() {
        return autorOntService;
    }

    public void setAutorOntService(AutorOntService autorOntService) {
        this.autorOntService = autorOntService;
    }

    public AutorProyectoOntService getAutorProyectoOntService() {
        return autorProyectoOntService;
    }

    public void setAutorProyectoOntService(AutorProyectoOntService autorProyectoOntService) {
        this.autorProyectoOntService = autorProyectoOntService;
    }

    public ProyectoOntService getProyectoOntService() {
        return proyectoOntService;
    }

    public void setProyectoOntService(ProyectoOntService proyectoOntService) {
        this.proyectoOntService = proyectoOntService;
    }

    public CarreraOntService getCarreraOntService() {
        return carreraOntService;
    }

    public void setCarreraOntService(CarreraOntService carreraOntService) {
        this.carreraOntService = carreraOntService;
    }

    public OfertaAcademicoOntService getOfertaAcademicoOntService() {
        return ofertaAcademicoOntService;
    }

    public void setOfertaAcademicoOntService(OfertaAcademicoOntService ofertaAcademicoOntService) {
        this.ofertaAcademicoOntService = ofertaAcademicoOntService;
    }

    public PeriodoAcademicoOntService getPeriodoAcademicoOntService() {
        return periodoAcademicoOntService;
    }

    public void setPeriodoAcademicoOntService(PeriodoAcademicoOntService periodoAcademicoOntService) {
        this.periodoAcademicoOntService = periodoAcademicoOntService;
    }

    public AreaAcademicaOntService getAreaAcademicaOntService() {
        return areaAcademicaOntService;
    }

    public void setAreaAcademicaOntService(AreaAcademicaOntService areaAcademicaOntService) {
        this.areaAcademicaOntService = areaAcademicaOntService;
    }

    public NivelAcademicoOntService getNivelAcademicoOntService() {
        return nivelAcademicoOntService;
    }

    public void setNivelAcademicoOntService(NivelAcademicoOntService nivelAcademicoOntService) {
        this.nivelAcademicoOntService = nivelAcademicoOntService;
    }

    public LineaInvestigacionOntService getLineaInvestigacionOntService() {
        return lineaInvestigacionOntService;
    }

    public void setLineaInvestigacionOntService(LineaInvestigacionOntService lineaInvestigacionOntService) {
        this.lineaInvestigacionOntService = lineaInvestigacionOntService;
    }

    public LineaInvestigacionProyectoOntService getLineaInvestigacionProyectoOntService() {
        return lineaInvestigacionProyectoOntService;
    }

    public void setLineaInvestigacionProyectoOntService(LineaInvestigacionProyectoOntService lineaInvestigacionProyectoOntService) {
        this.lineaInvestigacionProyectoOntService = lineaInvestigacionProyectoOntService;
    }

    public ProyectoCarreraOfertaOntService getProyectoCarreraOfertaOntService() {
        return proyectoCarreraOfertaOntService;
    }

    public void setProyectoCarreraOfertaOntService(ProyectoCarreraOfertaOntService proyectoCarreraOfertaOntService) {
        this.proyectoCarreraOfertaOntService = proyectoCarreraOfertaOntService;
    }

    public DocenteOntService getDocenteOntService() {
        return docenteOntService;
    }

    public void setDocenteOntService(DocenteOntService docenteOntService) {
        this.docenteOntService = docenteOntService;
    }

    public DirectorProyectoOntService getDirectorProyectoOntService() {
        return directorProyectoOntService;
    }

    public void setDirectorProyectoOntService(DirectorProyectoOntService directorProyectoOntService) {
        this.directorProyectoOntService = directorProyectoOntService;
    }

}
