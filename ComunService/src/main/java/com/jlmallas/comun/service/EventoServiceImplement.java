/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jlmallas.comun.service;

import com.jlmallas.comun.dao.EventoDao;
import com.jlmallas.comun.entity.Evento;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class EventoServiceImplement implements EventoService {

    @EJB
    private EventoDao eventoDao;

    @Override
    public void guardar(final Evento evento) {
        this.eventoDao.create(evento);
    }
    
    @Override
    public void actualizar(final Evento evento) {
        this.eventoDao.edit(evento);
    }
    
    @Override
    public void eliminar(final Evento evento) {
        this.eventoDao.remove(evento);
    }
    
    @Override
    public Evento buscarPorId(final Evento evento) {
        return this.eventoDao.find(evento.getId());
    }
    
    @Override
    public List<Evento> buscar(final Evento evento) {
        return this.eventoDao.buscar(evento);
    }
    
}
