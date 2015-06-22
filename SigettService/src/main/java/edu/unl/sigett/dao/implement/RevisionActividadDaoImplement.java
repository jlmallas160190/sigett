/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.RevisionActividadDao;
import edu.unl.sigett.entity.RevisionActividad;
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
public class RevisionActividadDaoImplement extends AbstractDao<RevisionActividad> implements RevisionActividadDao {

    public RevisionActividadDaoImplement() {
        super(RevisionActividad.class);
    }

    @Override
    public List<RevisionActividad> buscar(final RevisionActividad revisionActividad) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT r FROM RevisionActividad r WHERE 1=1 ");
        if (revisionActividad.getActividadId() != null) {
            sql.append(" and r.actividadId=:actividadId");
            parametros.put("actividadId", revisionActividad.getActividadId());
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
