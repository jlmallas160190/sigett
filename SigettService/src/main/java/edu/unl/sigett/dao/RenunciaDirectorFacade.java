/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.RenunciaDirector;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class RenunciaDirectorFacade extends AbstractDao<RenunciaDirector> implements RenunciaDirectorFacadeLocal {

    public RenunciaDirectorFacade() {
        super(RenunciaDirector.class);
    }

    @Override
    public List<RenunciaDirector> buscarPorDirectorProyecto(Long directorProyectoId) {
        try {
            Query query = em.createQuery("SELECT r FROM RenunciaDirector r WHERE " + "(r.directorProyectoId.id=:id)");
            query.setParameter("id", directorProyectoId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
