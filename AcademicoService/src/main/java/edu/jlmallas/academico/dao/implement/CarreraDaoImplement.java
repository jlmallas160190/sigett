/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao.implement;

import edu.jlmallas.academico.dao.AbstractDao;
import edu.jlmallas.academico.dao.CarreraDao;
import edu.jlmallas.academico.entity.Carrera;
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
public class CarreraDaoImplement extends AbstractDao<Carrera> implements CarreraDao {

    public CarreraDaoImplement() {
        super(Carrera.class);
    }

    @Override
    public Carrera buscarIdSga(String id) {
        List<Carrera> carreras = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT c from Carrera c where (c.idSga=:idSga)");
            query.setParameter("idSga", id);
            carreras = query.getResultList();
            return !carreras.isEmpty() ? carreras.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<Carrera> buscarPorCriterio(Carrera carrera) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<String, Object>();
        sql.append("Select c from Carrera c where 1=1 ");
        if (carrera.getAreaId() != null) {
            sql.append(" and c.areaId=:area");
            parametros.put("area", carrera.getAreaId());
        }
        if (carrera.getNombre() != null) {
            sql.append(" and c.nombre=:nombre");
            parametros.put("nombre", carrera.getNombre());
        }
        sql.append(" order by c.nombre asc ");
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }
}
