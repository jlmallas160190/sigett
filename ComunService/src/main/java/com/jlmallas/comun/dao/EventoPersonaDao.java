/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.dao;

import com.jlmallas.comun.entity.EventoPersona;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface EventoPersonaDao {

    void create(final EventoPersona eventoPersona);

    void edit(final EventoPersona eventoPersona);

    void remove(final EventoPersona eventoPersona);

    EventoPersona find(Object id);

    List<EventoPersona> findAll();

    List<EventoPersona> findRange(int[] range);

    List<EventoPersona> buscar(final EventoPersona eventoPersona);
}
