/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.dao.implement;

import com.jlmallas.comun.dao.AbstractDao;
import com.jlmallas.comun.dao.EventoDao;
import com.jlmallas.comun.entity.Evento;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class EventoDaoImplement extends AbstractDao<Evento> implements EventoDao {

    public EventoDaoImplement() {
        super(Evento.class);
    }

    @Override
    public List<Evento> buscar(Evento evento) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT e FROM Evento e  WHERE 1=1 ");
        if (evento.getCatalogoId() != null) {
            sql.append(" and e.catalogoId=:catalogoId");
            parametros.put("catalogoId", evento.getCatalogoId());
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
