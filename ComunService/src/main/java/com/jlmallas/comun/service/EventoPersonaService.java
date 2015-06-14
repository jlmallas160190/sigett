/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.service;

import com.jlmallas.comun.entity.EventoPersona;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface EventoPersonaService {

    void guardar(final EventoPersona eventoPersona);

    void actualizar(final EventoPersona eventoPersona);

    void eliminar(final EventoPersona eventoPersona);

    EventoPersona buscarPorId(final EventoPersona eventoPersona);

    List<EventoPersona> buscar(final EventoPersona eventoPersona);
}
