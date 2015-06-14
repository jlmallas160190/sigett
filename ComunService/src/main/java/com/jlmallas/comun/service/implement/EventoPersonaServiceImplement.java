/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.service.implement;

import com.jlmallas.comun.dao.EventoPersonaDao;
import com.jlmallas.comun.entity.EventoPersona;
import com.jlmallas.comun.service.EventoPersonaService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class EventoPersonaServiceImplement implements EventoPersonaService {

    @EJB
    private EventoPersonaDao eventoPersonaDao;

    @Override
    public void guardar(final EventoPersona eventoPersona) {
        this.eventoPersonaDao.create(eventoPersona);
    }

    @Override
    public void actualizar(final EventoPersona eventoPersona) {
        this.eventoPersonaDao.edit(eventoPersona);
    }

    @Override
    public void eliminar(final EventoPersona eventoPersona) {
        this.eventoPersonaDao.remove(eventoPersona);
    }

    @Override
    public EventoPersona buscarPorId(final EventoPersona eventoPersona) {
        return this.eventoPersonaDao.find(eventoPersona.getId());
    }

    @Override
    public List<EventoPersona> buscar(final EventoPersona eventoPersona) {
        return this.eventoPersonaDao.buscar(eventoPersona);
    }

}
