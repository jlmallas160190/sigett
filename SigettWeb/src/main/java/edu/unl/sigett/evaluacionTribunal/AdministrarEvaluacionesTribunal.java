/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.evaluacionTribunal;

import com.jlmallas.comun.dao.PersonaDao;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.proyecto.SessionProyecto;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.jlmallas.academico.entity.Docente;
import edu.unl.sigett.entity.EvaluacionTribunal;
import edu.unl.sigett.entity.MiembroTribunal;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.RangoEquivalencia;
import edu.unl.sigett.entity.RangoNota;
import edu.unl.sigett.entity.Tribunal;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import edu.unl.sigett.dao.AutorProyectoDao;
import edu.jlmallas.academico.dao.EstudianteCarreraDao;
import edu.unl.sigett.dao.EvaluacionTribunalDao;
import edu.unl.sigett.dao.MiembroTribunalDao;
import edu.unl.sigett.dao.ProyectoDao;
import edu.unl.sigett.dao.RangoEquivalenciaDao;
import edu.unl.sigett.dao.RangoNotaDao;
import org.jlmallas.seguridad.dao.UsuarioDao;
import edu.jlmallas.academico.dao.DocenteDao;
import edu.unl.sigett.enumeration.CatalogoEvaluacionEnum;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "consultaSustentaciones",
            pattern = "/consultaSustentaciones/",
            viewId = "/faces/pages/sigett/consultarSustentaciones.xhtml"
    )
})
public class AdministrarEvaluacionesTribunal implements Serializable {

//    @Inject
//    private SessionEvaluacionTribunal sessionEvaluacionTribunal;
//    @Inject
//    private SessionUsuario sessionUsuario;
//    @Inject
//    private SessionProyecto sessionProyecto;
//    @Inject
//    private AdministrarMiembrosTribunal administrarMiembrosTribunal;
//    @Inject
//    private AdministrarCalificacionMiembro administrarCalificacionMiembro;
//
//    @EJB
//    private UsuarioDao usuarioFacadeLocal;
//
//    @EJB
//    private RangoEquivalenciaDao rangoEquivalenciaFacadeLocal;
//    @EJB
//    private RangoNotaDao rangoNotaFacadeLocal;
//    @EJB
//    private EvaluacionTribunalDao evaluacionTribunalFacadeLocal;
//    @EJB
//    private ProyectoDao proyectoFacadeLocal;
//    @EJB
//    private MiembroTribunalDao miembroFacadeLocal;
//    @EJB
//    private ConfiguracionGeneralDao configuracionGeneralFacadeLocal;
//    @EJB
//    private AutorProyectoDao autorProyectoFacadeLocal;
//    @EJB
//    private EstudianteCarreraDao estudianteCarreraFacadeLocal;
//    @EJB
//    private PersonaDao personaFacadeLocal;
//    @EJB
//    private DocenteDao docenteFacadeLocal;
//
//    private boolean renderedEditar;
//    private boolean renderedNoEditar;
//    private boolean renderedEliminar;
//    private boolean renderedCrear;
//    private boolean renderedBuscar;
//    private boolean renderedDlgEditar;
//    private boolean renderedCalificarPublica;
//    private boolean renderedCalificarPrivada;
//    private boolean renderedAceptarNota;
//
//    private List<EvaluacionTribunal> evaluacionTribunals;
//    private List<EvaluacionTribunal> evaluacionTribunalesByCarrera;
//    private Docente docenteEncontrado;
//    private String rangoNota;
//    private ScheduleModel eventModel;
//    private ScheduleModel eventModelConsulta;
//    private EvaluacionTribunal consultaEvaluacionTribunal;
//
//    public AdministrarEvaluacionesTribunal() {
//    }
//
// 
//    
//    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
//    public EvaluacionTribunal getConsultaEvaluacionTribunal() {
//        return consultaEvaluacionTribunal;
//    }
//
//    public void setConsultaEvaluacionTribunal(EvaluacionTribunal consultaEvaluacionTribunal) {
//        this.consultaEvaluacionTribunal = consultaEvaluacionTribunal;
//    }
//
//    public ScheduleModel getEventModelConsulta() {
//        return eventModelConsulta;
//    }
//
//    public void setEventModelConsulta(ScheduleModel eventModelConsulta) {
//        this.eventModelConsulta = eventModelConsulta;
//    }
//
//    public boolean isRenderedAceptarNota() {
//        return renderedAceptarNota;
//    }
//
//    public void setRenderedAceptarNota(boolean renderedAceptarNota) {
//        this.renderedAceptarNota = renderedAceptarNota;
//    }
//
//    public boolean isRenderedCalificarPrivada() {
//        return renderedCalificarPrivada;
//    }
//
//    public void setRenderedCalificarPrivada(boolean renderedCalificarPrivada) {
//        this.renderedCalificarPrivada = renderedCalificarPrivada;
//    }
//
//    public Docente getDocenteEncontrado() {
//        return docenteEncontrado;
//    }
//
//    public void setDocenteEncontrado(Docente docenteEncontrado) {
//        this.docenteEncontrado = docenteEncontrado;
//    }
//
//    public List<EvaluacionTribunal> getEvaluacionTribunalesByCarrera() {
//        return evaluacionTribunalesByCarrera;
//    }
//
//    public void setEvaluacionTribunalesByCarrera(List<EvaluacionTribunal> evaluacionTribunalesByCarrera) {
//        this.evaluacionTribunalesByCarrera = evaluacionTribunalesByCarrera;
//    }
//
//    public ScheduleModel getEventModel() {
//        return eventModel;
//    }
//
//    public void setEventModel(ScheduleModel eventModel) {
//        this.eventModel = eventModel;
//    }
//
//    public boolean isRenderedCalificarPublica() {
//        return renderedCalificarPublica;
//    }
//
//    public void setRenderedCalificarPublica(boolean renderedCalificarPublica) {
//        this.renderedCalificarPublica = renderedCalificarPublica;
//    }
//
//    public SessionEvaluacionTribunal getSessionEvaluacionTribunal() {
//        return sessionEvaluacionTribunal;
//    }
//
//    public void setSessionEvaluacionTribunal(SessionEvaluacionTribunal sessionEvaluacionTribunal) {
//        this.sessionEvaluacionTribunal = sessionEvaluacionTribunal;
//    }
//
//    public boolean isRenderedEditar() {
//        return renderedEditar;
//    }
//
//    public void setRenderedEditar(boolean renderedEditar) {
//        this.renderedEditar = renderedEditar;
//    }
//
//    public boolean isRenderedNoEditar() {
//        return renderedNoEditar;
//    }
//
//    public void setRenderedNoEditar(boolean renderedNoEditar) {
//        this.renderedNoEditar = renderedNoEditar;
//    }
//
//    public boolean isRenderedEliminar() {
//        return renderedEliminar;
//    }
//
//    public void setRenderedEliminar(boolean renderedEliminar) {
//        this.renderedEliminar = renderedEliminar;
//    }
//
//    public boolean isRenderedCrear() {
//        return renderedCrear;
//    }
//
//    public void setRenderedCrear(boolean renderedCrear) {
//        this.renderedCrear = renderedCrear;
//    }
//
//    public boolean isRenderedBuscar() {
//        return renderedBuscar;
//    }
//
//    public void setRenderedBuscar(boolean renderedBuscar) {
//        this.renderedBuscar = renderedBuscar;
//    }
//
//    public boolean isRenderedDlgEditar() {
//        return renderedDlgEditar;
//    }
//
//    public void setRenderedDlgEditar(boolean renderedDlgEditar) {
//        this.renderedDlgEditar = renderedDlgEditar;
//    }
//
//    public List<EvaluacionTribunal> getEvaluacionTribunals() {
//        return evaluacionTribunals;
//    }
//
//    public void setEvaluacionTribunals(List<EvaluacionTribunal> evaluacionTribunals) {
//        this.evaluacionTribunals = evaluacionTribunals;
//    }
//
//    public String getRangoNota() {
//        return rangoNota;
//    }
//
//    public void setRangoNota(String rangoNota) {
//        this.rangoNota = rangoNota;
//    }
//
//    public void setSessionTribunal(Tribunal tribunal) {
//        try {
//            sessionEvaluacionTribunal.getEvaluacionTribunal().setTribunalId(tribunal);
//        } catch (Exception e) {
//        }
//    }
//
//    //</editor-fold>
}
