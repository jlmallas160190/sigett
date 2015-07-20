/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.ParametroCatalogoEvaluacionDao;
import edu.unl.sigett.entity.ParametroCatalogoEvaluacion;
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
public class ParametroCatalogoEvaluacionDaoImplement extends AbstractDao<ParametroCatalogoEvaluacion> implements ParametroCatalogoEvaluacionDao {

    public ParametroCatalogoEvaluacionDaoImplement() {
        super(ParametroCatalogoEvaluacion.class);
    }

    @Override
    public List<ParametroCatalogoEvaluacion> buscar(final ParametroCatalogoEvaluacion parametroCatalogoEvaluacion) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT pce FROM ParametroCatalogoEvaluacion pce WHERE 1=1");
        if (parametroCatalogoEvaluacion.getCatalogoEvaluacionId() != null) {
            sql.append(" and pce.catalogoEvaluacionId=:catalogoEvaluacionId");
            parametros.put("catalogoEvaluacionId", parametroCatalogoEvaluacion.getCatalogoEvaluacionId());
            existeFiltro = Boolean.TRUE;
        }
        if (parametroCatalogoEvaluacion.getParametroId() != null) {
            sql.append(" and pce.parametroId=:parametroId");
            parametros.put("parametroId", parametroCatalogoEvaluacion.getParametroId());
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
