/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao.implement;

import edu.jlmallas.academico.dao.AbstractDao;
import edu.jlmallas.academico.entity.EstadoLaboral;
import edu.jlmallas.academico.dao.EstadoLaboralDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class EstadoLaboralDaoImplement extends AbstractDao<EstadoLaboral> implements EstadoLaboralDao {

    public EstadoLaboralDaoImplement() {
        super(EstadoLaboral.class);
    }

    @Override
    public List<EstadoLaboral> buscar(EstadoLaboral estadoLaboral) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("SELECT e FROM EstadoLaboral e WHERE 1=1 ");
        if (estadoLaboral.getTipoContratoId() != null) {
            sql.append(" and e.tipoContratoId=:tc ");
            parametros.put("tc", estadoLaboral.getTipoContratoId());
        }
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }
}
