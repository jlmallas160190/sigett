/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.Director;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class DirectorFacade extends AbstractDao<Director> implements DirectorFacadeLocal {

    public DirectorFacade() {
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
}
