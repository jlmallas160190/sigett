/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.comun.eventoPersona;

import com.jlmallas.comun.entity.EventoPersona;
import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.service.EventoPersonaService;
import com.jlmallas.comun.service.EventoService;
import com.jlmallas.comun.service.ItemService;
import edu.unl.sigett.docenteUsuario.DocenteUsuarioDM;
import edu.unl.sigett.enumeration.CatalogoEventoEnum;
import edu.unl.sigett.enumeration.EstiloScheduleEnum;
import edu.unl.sigett.service.ActividadService;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;

/**
 *
 * @author jorge-luis
 */
@Named(value = "eventoPersonaController")
@SessionScoped
public class EventoPersonaController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionEventoPersona sessionEventoPersona;
    @Inject
    private DocenteUsuarioDM docenteUsuarioDM;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private EventoPersonaService eventoPersonaService;
    @EJB
    private ItemService itemService;
    @EJB
    private ActividadService actividadService;
    @EJB
    private EventoService eventoService;

    //</editor-fold>
    public EventoPersonaController() {
    }

    public void preRenderView() {
        this.buscar();
        this.generaSchedule();
    }

    /**
     * BUSCAR ACTIVIDADES ASIGANADAS A DIRECTOR.
     */
    private void buscar() {
        this.sessionEventoPersona.getEventoPersonas().clear();
        sessionEventoPersona.getEventoPersonas().addAll(eventoPersonaService.buscar(new EventoPersona(
                null, docenteUsuarioDM.getDocenteUsuarioDTO().getPersona(), null)));
    }

    /**
     * GENERAR CRONOGRAMA DE ACTIVIDADES
     */
    private void generaSchedule() {
        sessionEventoPersona.setSchedule(new DefaultScheduleModel());
        Item actividad = itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGOEVENTO.getTipo(), CatalogoEventoEnum.ACTIVIDAD.getTipo());
        for (EventoPersona eventoPersona : sessionEventoPersona.getEventoPersonas()) {
            DefaultScheduleEvent evento = new DefaultScheduleEvent();
            evento.setTitle(eventoPersona.getEvento().getNombre());
            evento.setStartDate(eventoPersona.getEvento().getFechaInicio());
            evento.setEndDate(eventoPersona.getEvento().getFechaFin());
            evento.setData(eventoPersona);
            if (eventoPersona.getEvento().getCatalogoId().equals(actividad.getId())) {
                evento.setStyleClass(EstiloScheduleEnum.ACTIVIDAD.getTipo());
                sessionEventoPersona.getSchedule().addEvent(evento);
                continue;
            }
            sessionEventoPersona.getSchedule().addEvent(evento);
        }
    }

    public void onEventSelect(SelectEvent selectEvent) {
        ScheduleEvent event = (ScheduleEvent) selectEvent.getObject();
        sessionEventoPersona.setEventoPersona((EventoPersona) event.getData());
        sessionEventoPersona.setRenderedCrud(Boolean.TRUE);
        RequestContext.getCurrentInstance().execute("PF('dlgEvento').show()");
    }

    public void onEventMove(ScheduleEntryMoveEvent moveEvent) {
        ScheduleEvent event = (ScheduleEvent) moveEvent.getScheduleEvent();
        EventoPersona eventoPersona = ((EventoPersona) event.getData());
        eventoService.actualizar(eventoPersona.getEvento());
    }

    public void onEventResize(ScheduleEntryResizeEvent resizeEvent) {
        ScheduleEvent event = (ScheduleEvent) resizeEvent.getScheduleEvent();
        EventoPersona eventoPersona = ((EventoPersona) event.getData());
        eventoService.actualizar(eventoPersona.getEvento());
    }

    public void cancelarEdicion() {
        sessionEventoPersona.setEventoPersona(new EventoPersona());
        sessionEventoPersona.setRenderedCrud(Boolean.FALSE);
        RequestContext.getCurrentInstance().execute("PF('dlgEvento').hide()");
    }
}
