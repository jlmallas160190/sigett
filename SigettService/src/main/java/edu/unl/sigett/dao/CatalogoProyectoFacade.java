/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.CatalogoProyecto;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class CatalogoProyectoFacade extends AbstractDao<CatalogoProyecto> implements CatalogoProyectoFacadeLocal {

    public CatalogoProyectoFacade() {
        super(CatalogoProyecto.class);
    }

    @Override
    public List<CatalogoProyecto> buscarPorCriterio(String criterio) {
        try {
            Query query = em.createQuery("Select c from CatalogoProyecto c where " + "(c.nombre like concat('%',:criterio,'%')) ");
            query.setParameter("criterio", criterio);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<CatalogoProyecto> buscarActivos() {
        try {
            Query query = em.createQuery("Select c from CatalogoProyecto c where c.esActivo=TRUE ");
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }
}
