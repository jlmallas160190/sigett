/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.CalificacionMiembroDao;
import edu.unl.sigett.entity.CalificacionMiembro;
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
public class CalificacionMiembroDaoImplement extends AbstractDao<CalificacionMiembro> implements CalificacionMiembroDao {

    public CalificacionMiembroDaoImplement() {
        super(CalificacionMiembro.class);
    }

    @Override
    public List<CalificacionMiembro> buscar(final CalificacionMiembro calificacionMiembro) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT c from CalificacionMiembro c WHERE 1=1");
        if (calificacionMiembro.getMiembroId() != null) {
            sql.append(" and c.miembroId=:miembroId ");
            parametros.put("miembroId", calificacionMiembro.getMiembroId());
            existeFiltro = Boolean.TRUE;
        }
        if (calificacionMiembro.getEvaluacionTribunalId() != null) {
            sql.append(" and c.evaluacionTribunalId=:evaluacionTribunalId ");
            parametros.put("evaluacionTribunalId", calificacionMiembro.getEvaluacionTribunalId());
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
