/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.AspiranteDao;
import edu.unl.sigett.entity.Aspirante;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class AspiranteDaoImplement extends AbstractDao<Aspirante> implements AspiranteDao {

    public AspiranteDaoImplement() {
        super(Aspirante.class);
    }

//    @Override
//    public List<Aspirante> aptos(Integer carreraId) {
//        try {
//            Query query = em.createQuery("SELECT a from Aspirante a WHERE" + " (a.esApto=TRUE AND a.estudianteCarrera.carreraId.id=:id)");
//            query.setParameter("id", carreraId);
//            return query.getResultList();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return null;
//    }
//
//    @Override
//    public List<Aspirante> buscarPorCarrera(Integer carreraId) {
//        try {
//            Query query = em.createQuery("SELECT a from  Aspirante a WHERE " + " (a.estudianteCarrera.carreraId.id=:id AND a.estudianteCarrera.esActivo=TRUE AND a.estudianteCarrera.estadoEstudianteCarrera.id != 3)");
//            query.setParameter("id", carreraId);
//            return query.getResultList();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return null;
//    }

    @Override
    public List<Aspirante> buscar(Aspirante aspirante) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("SELECT a from  Aspirante a WHERE 1=1 ");
        if (aspirante.getEsApto()) {
            sql.append(" and a.esActivo=:apto ");
            parametros.put("apto", aspirante.getEsApto());
        }
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }
}
