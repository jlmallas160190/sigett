/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.util;

import edu.jlmallas.academico.entity.OfertaAcademica;
import edu.jlmallas.academico.entity.PeriodoAcademico;
import edu.jlmallas.academico.service.OfertaAcademicaService;
import edu.jlmallas.academico.service.PeriodoAcademicoService;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;

/**
 *
 * @author jorge-luis
 */
@Named(value = "selectItemsController")
@SessionScoped
public class SelectItemsController implements Serializable {

    @Inject
    private SessionSelectItems sessionSelectItems;
    @Inject
    private CabeceraController cabeceraController;
    @EJB(lookup = "java:global/AcademicoService/OfertaAcademicaServiceImplement!edu.jlmallas.academico.service.OfertaAcademicaService")
    private OfertaAcademicaService ofertaAcademicaService;
    @EJB(lookup = "java:global/AcademicoService/PeriodoAcademicoServiceImplement!edu.jlmallas.academico.service.PeriodoAcademicoService")
    private PeriodoAcademicoService periodoAcademicoService;

    public SelectItemsController() {

    }

    public void preRenderView() {
        this.listadoOfertasAcademicas();
        this.listadoPeriodosAcademicos();
    }

    private void listadoOfertasAcademicas() {
        sessionSelectItems.getOfertaAcademicas().clear();
        sessionSelectItems.getFilterOfertaAcademicas().clear();
        List<OfertaAcademica> ofertaAcademicas = ofertaAcademicaService.buscar(new OfertaAcademica());
        for (OfertaAcademica ofertaAcademica : ofertaAcademicas) {
            PeriodoAcademico periodoAcademico = ofertaAcademica.getPeriodoAcademicoId();
            periodoAcademico.setNombre(cabeceraController.getUtilService().formatoFecha(periodoAcademico.getFechaInicio(), "MMMM/yyyy/dd") + " - "
                    + cabeceraController.getUtilService().formatoFecha(periodoAcademico.getFechaFin(), "MMMM/yyyy/dd"));
            sessionSelectItems.getOfertaAcademicas().add(ofertaAcademica);
        }
        this.sessionSelectItems.setFilterOfertaAcademicas(sessionSelectItems.getOfertaAcademicas());
    }

    private void listadoPeriodosAcademicos() {
        sessionSelectItems.getPeriodoAcademicos().clear();
        List<PeriodoAcademico> periodoAcademicos = periodoAcademicoService.buscarPorCriterio(new PeriodoAcademico());
        for (PeriodoAcademico periodoAcademico : periodoAcademicos) {
            periodoAcademico.setNombre(cabeceraController.getUtilService().formatoFecha(periodoAcademico.getFechaInicio(), "MMMM/yyyy/dd") + " - "
                    + cabeceraController.getUtilService().formatoFecha(periodoAcademico.getFechaFin(), "MMMM/yyyy/dd"));
            sessionSelectItems.getPeriodoAcademicos().add(periodoAcademico);
        }
    }
}
