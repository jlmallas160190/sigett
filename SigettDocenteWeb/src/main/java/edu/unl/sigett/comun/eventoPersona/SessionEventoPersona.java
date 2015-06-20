/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.comun.eventoPersona;

import com.jlmallas.comun.entity.EventoPersona;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author jorge-luis
 */
@Named(value = "sessionEventoPersona")
@SessionScoped
public class SessionEventoPersona implements Serializable {

    private EventoPersona eventoPersona;

    private List<EventoPersona> eventoPersonas;

    private ScheduleModel schedule;

    private Boolean renderedCrud;

    public SessionEventoPersona() {
        this.eventoPersonas = new ArrayList<>();
        this.eventoPersona = new EventoPersona();
    }

    public EventoPersona getEventoPersona() {
        return eventoPersona;
    }

    public void setEventoPersona(EventoPersona eventoPersona) {
        this.eventoPersona = eventoPersona;
    }

    public ScheduleModel getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleModel schedule) {
        this.schedule = schedule;
    }

    public List<EventoPersona> getEventoPersonas() {
        return eventoPersonas;
    }

    public void setEventoPersonas(List<EventoPersona> eventoPersonas) {
        this.eventoPersonas = eventoPersonas;
    }

    public Boolean getRenderedCrud() {
        return renderedCrud;
    }

    public void setRenderedCrud(Boolean renderedCrud) {
        this.renderedCrud = renderedCrud;
    }

}
