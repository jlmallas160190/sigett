/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.EvaluacionTribunalDao;
import edu.unl.sigett.entity.EvaluacionTribunal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class EvaluacionTribunalDaoImplement extends AbstractDao<EvaluacionTribunal> implements EvaluacionTribunalDao {

    public EvaluacionTribunalDaoImplement() {
        super(EvaluacionTribunal.class);
    }

    @Override
    public List<EvaluacionTribunal> buscar(final EvaluacionTribunal evaluacionTribunal) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT ev FROM EvaluacionTribunal ev WHERE 1=1");
        if (evaluacionTribunal.getEsAptoCalificar() != null) {
            sql.append(" and ev.esAptoCalificar=:esApto ");
            parametros.put("esApto", evaluacionTribunal.getEsAptoCalificar());
            existeFiltro = Boolean.TRUE;
        }
        if (evaluacionTribunal.getCatalogoEvaluacionId() != null) {
            sql.append(" and ev.catalogoEvaluacionId=:catalogoEvaluacionId");
            parametros.put("catalogoEvaluacionId", evaluacionTribunal.getCatalogoEvaluacionId());
            existeFiltro = Boolean.TRUE;
        }
        if (evaluacionTribunal.getEsActivo() != null) {
            sql.append(" and ev.esActivo=:activo");
            parametros.put("activo", evaluacionTribunal.getEsActivo());
            existeFiltro = Boolean.TRUE;
        }
        if (evaluacionTribunal.getTribunalId() != null) {
            sql.append(" and ev.tribunalId=:tribunalId");
            parametros.put("tribunalId", evaluacionTribunal.getTribunalId());
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
