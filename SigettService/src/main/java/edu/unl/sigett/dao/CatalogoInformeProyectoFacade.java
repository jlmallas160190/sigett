/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.CatalogoInformeProyecto;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class CatalogoInformeProyectoFacade extends AbstractDao<CatalogoInformeProyecto> implements CatalogoInformeProyectoFacadeLocal {

    public CatalogoInformeProyectoFacade() {
        super(CatalogoInformeProyecto.class);
    }

    @Override
    public List<CatalogoInformeProyecto> buscarActivos() {
        try {
            Query query = em.createQuery("SELECT c FROM CatalogoInformeProyecto c WHERE" + " (c.esActivo=TRUE)");
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
