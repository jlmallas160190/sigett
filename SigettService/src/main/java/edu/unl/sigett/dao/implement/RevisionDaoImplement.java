/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.RevisionDao;
import edu.unl.sigett.entity.Revision;
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
public class RevisionDaoImplement extends AbstractDao<Revision> implements RevisionDao {

    public RevisionDaoImplement() {
        super(Revision.class);
    }

    @Override
    public List<Revision> buscar(final Revision revision) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT r FROM Revision r WHERE 1=1 ");
        if (revision.getRevisionActividad() != null) {
            sql.append(" and r.revisionActividad=:revisionActividad");
            parametros.put("revisionActividad", revision.getRevisionActividad());
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
