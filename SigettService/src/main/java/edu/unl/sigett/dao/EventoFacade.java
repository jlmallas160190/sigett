/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.Evento;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class EventoFacade extends AbstractDao<Evento> implements EventoFacadeLocal {

    public EventoFacade() {
        super(Evento.class);
    }

    @Override
    public Evento buscarPorCategoriaEvento(int categoriaId, String eventoId) {
        try {
            List<Evento> eventos = new ArrayList<>();
            Query query = em.createQuery("SELECT e FROM Evento e WHERE " + "(e.catalogoEventoId.id=:id and e.eventoId=:eventoId)");
            query.setParameter("eventoId", eventoId);
            query.setParameter("id", categoriaId);
            eventos = query.getResultList();
            return !eventos.isEmpty() ? eventos.get(0) : null;
        } catch (Exception e) {
        }
        return null;
    }

}