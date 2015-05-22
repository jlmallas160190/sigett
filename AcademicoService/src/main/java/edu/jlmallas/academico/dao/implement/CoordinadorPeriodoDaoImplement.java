/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao.implement;

import edu.jlmallas.academico.entity.CoordinadorPeriodo;
import edu.jlmallas.academico.dao.AbstractDao;
import edu.jlmallas.academico.dao.CoordinadorPeriodoDao;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class CoordinadorPeriodoDaoImplement extends AbstractDao<CoordinadorPeriodo> implements CoordinadorPeriodoDao {

    public CoordinadorPeriodoDaoImplement() {
        super(CoordinadorPeriodo.class);
    }

//    @Override
//    public List<CoordinadorPeriodo> buscarPorCarrera(Integer carreraId) {
//        try {
//            Query query = em.createQuery("SELECT c from CoordinadorPeriodo c WHERE " + "(c.periodoId.carreraId.id=:id)");
//            query.setParameter("id", carreraId);
//            return query.getResultList();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return null;
//    }
//
//    @Override
//    public CoordinadorPeriodo buscarVigente(Integer carreraId) {
//        try {
//            List<CoordinadorPeriodo> coordinadorPeriodos = new ArrayList<>();
//            Query query = em.createQuery("SELECT c from CoordinadorPeriodo c WHERE " + "(c.periodoId.carreraId.id=:id and c.esVigente=TRUE)");
//            query.setParameter("id", carreraId);
//            coordinadorPeriodos = query.getResultList();
//            return !coordinadorPeriodos.isEmpty() ? coordinadorPeriodos.get(0) : null;
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return null;
//    }
    @Override
    public List<CoordinadorPeriodo> buscar(CoordinadorPeriodo coordinadorPeriodo) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("SELECT c from CoordinadorPeriodo c WHERE 1=1 ");
        if (coordinadorPeriodo.getPeriodoId().getCarreraId() != null) {
            sql.append(" and c.periodoId.carreraId=:carreraId ");
            parametros.put("carreraId", coordinadorPeriodo.getPeriodoId().getCarreraId());
        }
        if (coordinadorPeriodo.getCoordinadorId() != null) {
            sql.append(" and c.coordinador=:coordinadorId");
            parametros.put("coordinadorId", coordinadorPeriodo.getCoordinadorId());
        }
        if (coordinadorPeriodo.getEsVigente() != null) {
            sql.append(" and c.esVigente=:vigente");
            parametros.put("vigente", coordinadorPeriodo.getEsVigente());
        }
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }
}
