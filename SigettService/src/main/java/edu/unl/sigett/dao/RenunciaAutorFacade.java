/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.RenunciaAutor;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class RenunciaAutorFacade extends AbstractDao<RenunciaAutor> implements RenunciaAutorFacadeLocal {

    public RenunciaAutorFacade() {
        super(RenunciaAutor.class);
    }

    @Override
    public List<RenunciaAutor> buscarPorAutorProyecto(Long autorId) {
        try {
            Query query = em.createQuery("SELECT r FROM RenunciaAutor r WHERE " + "(r.autorProyectoId.id=:id)");
            query.setParameter("id", autorId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
