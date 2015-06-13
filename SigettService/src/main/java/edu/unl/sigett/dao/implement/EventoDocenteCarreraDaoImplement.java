/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.EventoDocenteCarreraDao;
import edu.unl.sigett.entity.EventoDocenteCarrera;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class EventoDocenteCarreraDaoImplement extends AbstractDao<EventoDocenteCarrera> implements EventoDocenteCarreraDao {

    public EventoDocenteCarreraDaoImplement() {
        super(EventoDocenteCarrera.class);
    }

    @Override
    public List<EventoDocenteCarrera> buscar(EventoDocenteCarrera eventoDocenteCarrera) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT e from  EventoDocenteCarrera e WHERE 1=1 ");
        if (eventoDocenteCarrera.getDocenteCarreraId() != null) {
            sql.append(" and e.docenteCarreraId=:docenteCarreraId ");
            parametros.put("docenteCarreraId", eventoDocenteCarrera.getDocenteCarreraId());
            existeFiltro = Boolean.TRUE;
        }
        if (eventoDocenteCarrera.getEventoId() != null) {
            sql.append(" and e.eventoId=:eventoId ");
            parametros.put("eventoId", eventoDocenteCarrera.getEventoId());
            existeFiltro = Boolean.TRUE;
        }
        if (eventoDocenteCarrera.getTablaId() != null) {
            sql.append(" and e.eventoId=:tablaId ");
            parametros.put("tablaId", eventoDocenteCarrera.getTablaId());
            existeFiltro = Boolean.TRUE;
        }
        if (!existeFiltro) {
            return new ArrayList<>();
        }
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }

}
