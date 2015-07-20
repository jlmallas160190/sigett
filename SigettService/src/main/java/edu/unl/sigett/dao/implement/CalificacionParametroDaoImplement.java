/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.CalificacionParametroDao;
import edu.unl.sigett.entity.CalificacionParametro;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class CalificacionParametroDaoImplement extends AbstractDao<CalificacionParametro> implements CalificacionParametroDao {

    public CalificacionParametroDaoImplement() {
        super(CalificacionParametro.class);
    }

    @Override
    public List<CalificacionParametro> buscar(final CalificacionParametro calificacionParametro) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT cp FROM CalificacionParametro cp WHERE 1=1");
        if (calificacionParametro.getCalificacionMiembroId() != null) {
            sql.append(" and cp.calificacionMiembroId=:calificacionMiembroId");
            parametros.put("calificacionMiembroId", calificacionParametro.getCalificacionMiembroId());
            existeFiltro = Boolean.TRUE;
        }
        if (calificacionParametro.getParametroCatEvId() != null) {
            sql.append(" and cp.parametroCatEvId=:parametroCatEvId");
            parametros.put("parametroCatEvId", calificacionParametro.getParametroCatEvId());
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
