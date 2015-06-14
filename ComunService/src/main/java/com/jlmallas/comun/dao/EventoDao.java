/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.dao;

import com.jlmallas.comun.entity.Evento;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface EventoDao {

    void create(final Evento evento);

    void edit(final Evento evento);

    void remove(final Evento evento);

    Evento find(Object id);

    List<Evento> findAll();

    List<Evento> findRange(int[] range);

    List<Evento> buscar(final Evento evento);
}
