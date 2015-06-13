/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.dao.EventoDocenteCarreraDao;
import edu.unl.sigett.entity.EventoDocenteCarrera;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class EventoDocenteCarreraServiceImplement implements EventoDocenteCarreraService {

    @EJB
    private EventoDocenteCarreraDao eventoDocenteCarreraDao;

    @Override
    public void guardar(final EventoDocenteCarrera eventoDocenteCarrera) {
        this.eventoDocenteCarreraDao.create(eventoDocenteCarrera);
    }

    @Override
    public void actualizar(final EventoDocenteCarrera eventoDocenteCarrera) {
        this.eventoDocenteCarreraDao.edit(eventoDocenteCarrera);
    }

    @Override
    public void eliminar(final EventoDocenteCarrera eventoDocenteCarrera) {
        this.eventoDocenteCarreraDao.remove(eventoDocenteCarrera);
    }

    @Override
    public EventoDocenteCarrera buscarPorId(final EventoDocenteCarrera eventoDocenteCarrera) {
        return this.eventoDocenteCarreraDao.find(eventoDocenteCarrera.getId());
    }

    @Override
    public List<EventoDocenteCarrera> buscar(final EventoDocenteCarrera eventoDocenteCarrera) {
        return this.eventoDocenteCarreraDao.buscar(eventoDocenteCarrera);
    }

}
