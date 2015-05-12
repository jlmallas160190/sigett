/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.CatalogoDuracion;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class CatalogoDuracionFacade extends AbstractDao<CatalogoDuracion> implements CatalogoDuracionFacadeLocal {

    public CatalogoDuracionFacade() {
        super(CatalogoDuracion.class);
    }

    @Override
    public List<CatalogoDuracion> buscarActivos() {
        try {
            Query query = em.createQuery("Select c from CatalogoDuracion c where c.esActivo=TRUE ");
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }
}
