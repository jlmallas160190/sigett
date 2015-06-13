/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.EventoDocenteCarrera;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface EventoDocenteCarreraDao {

    void create(final EventoDocenteCarrera eventoDocenteCarrera);

    void edit(final EventoDocenteCarrera eventoDocenteCarrera);

    void remove(final EventoDocenteCarrera eventoDocenteCarrera);

    EventoDocenteCarrera find(Object id);

    public List<EventoDocenteCarrera> buscar(final EventoDocenteCarrera eventoDocenteCarrera);
}
