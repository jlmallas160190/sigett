/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.dao.implement;

import com.jlmallas.comun.dao.AbstractDao;
import com.jlmallas.comun.dao.EventoPersonaDao;
import com.jlmallas.comun.entity.EventoPersona;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class EventoPersonaDaoImplement extends AbstractDao<EventoPersona> implements EventoPersonaDao {

    public EventoPersonaDaoImplement() {
        super(EventoPersona.class);
    }

    @Override
    public List<EventoPersona> buscar(EventoPersona eventoPersona) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT e FROM EventoPersona e  WHERE 1=1 ");
        if (eventoPersona.getPersonaId() != null) {
            sql.append(" and e.personaId=:personaId");
            parametros.put("personaId", eventoPersona.getPersonaId());
            existeFiltro = Boolean.TRUE;
        }
        if (eventoPersona.getTablaId() != null) {
            sql.append(" and e.tablaId=:tablaId");
            parametros.put("tablaId", eventoPersona.getTablaId());
            existeFiltro = Boolean.TRUE;
        }

        if (!existeFiltro) {
            return null;
        }
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }

}
