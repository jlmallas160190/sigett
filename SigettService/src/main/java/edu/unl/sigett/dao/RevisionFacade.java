/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.Revision;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class RevisionFacade extends AbstractDao<Revision> implements RevisionFacadeLocal {

    public RevisionFacade() {
        super(Revision.class);
    }

    @Override
    public List<Revision> buscarPorActividad(Long actividadId) {
        try {
            Query query = em.createQuery("SELECT r from Revision r WHERE" + " (r.actividadId.id=:id)");
            query.setParameter("id", actividadId);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;

    }
}