/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao.implement;

import edu.jlmallas.academico.dao.DocenteCarreraDao;
import edu.jlmallas.academico.dao.AbstractDao;
import edu.jlmallas.academico.entity.DocenteCarrera;
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
public class DocenteCarreraDaoImplement extends AbstractDao<DocenteCarrera> implements DocenteCarreraDao {

    public DocenteCarreraDaoImplement() {
        super(DocenteCarrera.class);
    }

    @Override
    public List<DocenteCarrera> buscar(DocenteCarrera docenteCarrera) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT d from DocenteCarrera d WHERE 1=1 ");
        if (docenteCarrera.getCarreraId() != null) {
            sql.append(" and d.carreraId=:carreraId ");
            parametros.put("carreraId", docenteCarrera.getCarreraId());
            existeFiltro = Boolean.TRUE;
        }
        if (docenteCarrera.getOfertaAcademica() != null) {
            sql.append(" and d.ofertaAcademica=:ofertaAcademica");
            parametros.put("ofertaAcademica", docenteCarrera.getOfertaAcademica());
            existeFiltro = Boolean.TRUE;
        }
        if (docenteCarrera.getDocenteId() != null) {
            sql.append(" and d.docenteId=:docenteId");
            parametros.put("docenteId", docenteCarrera.getDocenteId());
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

//    @Override
//    public List<DocenteCarrera> buscarPorDocente(Long docenteId) {
//        try {
//            Query query = em.createQuery("SELECT d from DocenteCarrera d WHERE " + "(d.docenteId.id=:id)");
//            query.setParameter("id", docenteId);
//            return query.getResultList();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return null;
//    }
}
