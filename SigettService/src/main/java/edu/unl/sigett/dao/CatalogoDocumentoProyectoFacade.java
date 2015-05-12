/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.CatalogoDocumentoProyecto;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class CatalogoDocumentoProyectoFacade extends AbstractDao<CatalogoDocumentoProyecto> implements CatalogoDocumentoProyectoFacadeLocal {

    public CatalogoDocumentoProyectoFacade() {
        super(CatalogoDocumentoProyecto.class);
    }

    @Override
    public List<CatalogoDocumentoProyecto> buscarActivos() {
        try {
            Query query = em.createQuery("SELECT c FROM CatalogoDocumentoProyecto c WHERE" + " (c.esActivo=TRUE)");
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
