/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.EventoDocenteCarrera;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface EventoDocenteCarreraService {

    void guardar(final EventoDocenteCarrera eventoDocenteCarrera);

    void actualizar(final EventoDocenteCarrera eventoDocenteCarrera);

    void eliminar(final EventoDocenteCarrera eventoDocenteCarrera);

    EventoDocenteCarrera buscarPorId(final EventoDocenteCarrera eventoDocenteCarrera);

    List<EventoDocenteCarrera> buscar(final EventoDocenteCarrera eventoDocenteCarrera);
}
