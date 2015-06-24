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
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.jlmallas.academico.service.DocenteCarreraService;
import edu.unl.sigett.actividad.SessionActividad;
import edu.unl.sigett.director.DirectorDTO;
import edu.unl.sigett.directorProyecto.DirectorProyectoDTO;
import edu.unl.sigett.directorProyecto.SessionDirectorProyecto;
import edu.unl.sigett.docenteUsuario.DocenteUsuarioDM;
import edu.unl.sigett.entity.Actividad;
import edu.unl.sigett.entity.DirectorProyecto;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.enumeration.CatalogoEventoEnum;
import edu.unl.sigett.enumeration.EstiloScheduleEnum;
import edu.unl.sigett.service.ActividadService;
import edu.unl.sigett.service.DirectorProyectoService;
import edu.unl.sigett.service.ProyectoService;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
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
    @Inject
    private SessionDirectorProyecto sessionDirectorProyecto;
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
    @EJB
    private DirectorProyectoService directorProyectoService;
    @EJB
    private ProyectoService proyectoService;
    @EJB
    private DocenteCarreraService docenteCarreraService;

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
        List<EventoPersona> eventoPersonas = eventoPersonaService.buscar(new EventoPersona(
                null, docenteUsuarioDM.getDocenteUsuarioDTO().getPersona(), null));
        for (EventoPersona eventoPersona : eventoPersonas) {
            List<EventoPersona> otrosEventoPersonas = eventoPersonaService.buscar(new EventoPersona(null, null, eventoPersona.getEvento()));
            for (EventoPersona ev : otrosEventoPersonas) {
                if (ev.getPersonaId().equals(docenteUsuarioDM.getDocenteUsuarioDTO().getPersona())) {
                    continue;
                }
                ev.getEvento().setInvitados("");
                ev.getEvento().setInvitados(ev.getEvento().getInvitados().concat(" ") + ev.getPersonaId().getNombres().concat(" ")
                        + ev.getPersonaId().getApellidos().concat("\n"));
                if (!sessionEventoPersona.getEventoPersonas().contains(ev)) {
                    sessionEventoPersona.getEventoPersonas().add(ev);
                }
            }
        }
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

    public String editar() {
        Item catalogo = itemService.buscarPorId(sessionEventoPersona.getEventoPersona().getEvento().getCatalogoId());
        if (catalogo.getCodigo().equals(CatalogoEventoEnum.ACTIVIDAD.getTipo())) {
            Actividad actividad = actividadService.buscarPorId(new Actividad(sessionEventoPersona.getEventoPersona().getEvento().getTablaId()));
            DirectorProyecto directorProyectoBuscar = new DirectorProyecto();
            Proyecto proyecto = proyectoService.buscarPorId(new Proyecto(actividad.getCronogramaId().getId()));
            directorProyectoBuscar.setProyectoId(proyecto);
            List<DirectorProyecto> directorProyectos = directorProyectoService.buscar(directorProyectoBuscar);
            if (directorProyectos == null) {
                return "";
            }
            DirectorProyecto directorProyecto = !directorProyectos.isEmpty() ? directorProyectos.get(0) : null;
            if (directorProyecto == null) {
                return "";
            }
            DirectorProyectoDTO directorProyectoDTO = new DirectorProyectoDTO(directorProyecto, new DirectorDTO(directorProyecto.getDirectorId(),
                    docenteCarreraService.buscarPorId(new DocenteCarrera(directorProyecto.getDirectorId().getId())), null));
            sessionDirectorProyecto.setDirectorProyectoDTO(directorProyectoDTO);
            return "pretty:editarDirectorProyecto";
        }
        return "";
    }

    public void cancelarEdicion() {
        sessionEventoPersona.setEventoPersona(new EventoPersona());
        sessionEventoPersona.setRenderedCrud(Boolean.FALSE);
        RequestContext.getCurrentInstance().execute("PF('dlgEvento').hide()");
    }
}
