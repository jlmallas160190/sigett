/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.DirectorDao;
import edu.unl.sigett.entity.Director;
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
public class DirectorDaoImplement extends AbstractDao<Director> implements DirectorDao {

    public DirectorDaoImplement() {
        super(Director.class);
    }

    @Override
    public List<Director> buscarAptos(Integer carreraId) {
        try {
            Query query = em.createQuery("SELECT d from Director d WHERE" + " (d.docenteCarrera.carreraId.id=:id)");
            query.setParameter("id", carreraId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<Director> buscar(final Director director) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT d from Director d WHERE 1=1 ");
        if (director.getEsActivo() != null) {
            sql.append(" and d.esActivo=:activo");
            parametros.put("activo", director.getEsActivo());
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
