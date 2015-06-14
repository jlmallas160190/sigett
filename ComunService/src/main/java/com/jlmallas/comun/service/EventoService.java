/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.service;

import com.jlmallas.comun.entity.Evento;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface EventoService {

    void guardar(final Evento evento);

    void actualizar(final Evento evento);

    void eliminar(final Evento evento);

    Evento buscarPorId(final Evento evento);

    List<Evento> buscar(final Evento evento);

}
