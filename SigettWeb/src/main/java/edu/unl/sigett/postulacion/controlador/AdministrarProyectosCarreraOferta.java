/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.postulacion.controlador;

import edu.unl.sigett.comun.controlador.AdministrarLineasInvestigacionCarrera;
import edu.unl.sigett.adjudicacion.controlador.AdministrarDirectoresProyecto;
import edu.unl.sigett.postulacion.managed.session.SessionConsultarProyecto;
import edu.unl.sigett.academico.controlador.AdministrarCarreras;
import edu.jlmallas.academico.entity.Area;
import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionCarrera;
import edu.jlmallas.academico.entity.OfertaAcademica;
import edu.jlmallas.academico.entity.PeriodoAcademico;
import edu.unl.sigett.entity.Proyecto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import edu.jlmallas.academico.service.AreaService;
import edu.jlmallas.academico.service.OfertaAcademicaFacadeLocal;
import edu.unl.sigett.session.ProyectoFacadeLocal;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class AdministrarProyectosCarreraOferta implements Serializable {

    @Inject
    private AdministrarCarreras administrarCarreras;
    private String tema;
    private Carrera carrera;
    private PeriodoAcademico periodoAcademico;
    private LineaInvestigacion li;
    @EJB
    private OfertaAcademicaFacadeLocal ofertaAcademicaFacadeLocal;
    private List<Proyecto> proyectos;
    private boolean selectByEstado;
    private boolean selectByCategoria;
    private boolean selectByPeriodo;
    private boolean selectByCarrera;
    private boolean selectByLineaInvestigacion;
    private String categoria;
    private String estado;
    private String byArea;
    @EJB
    private ProyectoFacadeLocal proyectoFacadeLocal;
    @EJB
    private AreaService areaFacadeLocal;
    private List<Carrera> carreras;
    private List<LineaInvestigacionCarrera> lineaInvestigacionCarreras;
    @Inject
    private SessionConsultarProyecto sessionConsultarProyecto;
    @Inject
    private AdministrarAutoresProyecto administrarAutoresProyecto;
    @Inject
    private AdministrarDirectoresProyecto administrarDirectoresProyecto;
    @Inject
    private AdministrarLineasInvestigacionCarrera administrarLineasInvestigacionCarrera;

    public AdministrarProyectosCarreraOferta() {
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public String iniciarBusquedaPorCarrera(Carrera carrera) {
        String navegacion = "";
        try {
            this.tema = "";
            this.proyectos = new ArrayList<>();
            navegacion = "directorioProyectosCarrera?faces-redirect=true";
        } catch (Exception e) {
        }
        return navegacion;
    }

    public String editar(Proyecto proyecto) {
        String navegacion = "";
        try {
            sessionConsultarProyecto.setProyecto(proyecto);
            administrarAutoresProyecto.listadoAutores("", proyecto);
            administrarDirectoresProyecto.historialDirectoresProyecto("", proyecto);
            navegacion = "verProyecto?faces-redirect=true";
        } catch (Exception e) {
        }
        return navegacion;
    }

    public void buscarLineasInvestigacionPorCarrera(Carrera carrera) {
        try {
            this.lineaInvestigacionCarreras = new ArrayList<>();
            lineaInvestigacionCarreras = administrarLineasInvestigacionCarrera.buscarByCarrera(carrera);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void buscarCarreras(String area) {
        try {
            this.carreras = new ArrayList<>();
            int pos = area.indexOf(":");
            Area a = areaFacadeLocal.buscarPorId(Integer.parseInt(area.substring(0, pos)));
            if (a != null) {
                carreras = administrarCarreras.listarPorArea(a);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public List<PeriodoAcademico> listadoPeriodosAcademicos() {
        List<PeriodoAcademico> periodoAcademicos = new ArrayList<>();
        try {
            for (OfertaAcademica ofertaAcademica : ofertaAcademicaFacadeLocal.findAll()) {
                if (contienePeriodoAcademico(periodoAcademicos, ofertaAcademica.getPeriodoAcademicoId()) == false) {
                    periodoAcademicos.add(ofertaAcademica.getPeriodoAcademicoId());
                }
            }
        } catch (Exception e) {
        }
        return periodoAcademicos;
    }

    public boolean contienePeriodoAcademico(List<PeriodoAcademico> periodoAcademicos, PeriodoAcademico p) {
        boolean var = false;
        for (PeriodoAcademico pa : periodoAcademicos) {
            if (pa.equals(p)) {
                var = true;
                break;
            }
        }
        return var;
    }

    public void editarLineaInvestigacion(LineaInvestigacion lineaInvestigacion) {
        try {
//            li = lineaInvestigacion;
        } catch (Exception e) {
        }
    }

    public void editarPeriodo(PeriodoAcademico p) {
        try {
//            periodoAcademico = p;
        } catch (Exception e) {
        }
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

//    public Carrera getCarrera() {
//        return carrera;
//    }
//
//    public void setCarrera(Carrera carrera) {
//        this.carrera = carrera;
//    }
//
//    public PeriodoAcademico getPeriodoAcademico() {
//        return periodoAcademico;
//    }
//
//    public void setPeriodoAcademico(PeriodoAcademico periodoAcademico) {
//        this.periodoAcademico = periodoAcademico;
//    }
//
//    public LineaInvestigacion getLi() {
//        return li;
//    }
//    public void setLi(LineaInvestigacion li) {
//        this.li = li;
//    }
    public List<Carrera> getCarreras() {
        return carreras;
    }

    public void setCarreras(List<Carrera> carreras) {
        this.carreras = carreras;
    }

    public boolean isSelectByCarrera() {
        return selectByCarrera;
    }

    public void setSelectByCarrera(boolean selectByCarrera) {
        this.selectByCarrera = selectByCarrera;
    }

    public boolean isSelectByLineaInvestigacion() {
        return selectByLineaInvestigacion;
    }

    public void setSelectByLineaInvestigacion(boolean selectByLineaInvestigacion) {
        this.selectByLineaInvestigacion = selectByLineaInvestigacion;
    }

    public String getByArea() {
        return byArea;
    }

    public void setByArea(String byArea) {
        this.byArea = byArea;
    }

    public boolean isSelectByEstado() {
        return selectByEstado;
    }

    public void setSelectByEstado(boolean selectByEstado) {
        this.selectByEstado = selectByEstado;
    }

    public boolean isSelectByCategoria() {
        return selectByCategoria;
    }

    public void setSelectByCategoria(boolean selectByCategoria) {
        this.selectByCategoria = selectByCategoria;
    }

    public boolean isSelectByPeriodo() {
        return selectByPeriodo;
    }

    public void setSelectByPeriodo(boolean selectByPeriodo) {
        this.selectByPeriodo = selectByPeriodo;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public List<Proyecto> getProyectos() {
        return proyectos;
    }

    public void setProyectos(List<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }

    public List<LineaInvestigacionCarrera> getLineaInvestigacionCarreras() {
        return lineaInvestigacionCarreras;
    }

    public void setLineaInvestigacionCarreras(List<LineaInvestigacionCarrera> lineaInvestigacionCarreras) {
        this.lineaInvestigacionCarreras = lineaInvestigacionCarreras;
    }
//</editor-fold>

}
