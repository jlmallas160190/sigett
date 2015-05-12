/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.CatalogoEvaluacion;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class CatalogoEvaluacionFacade extends AbstractDao<CatalogoEvaluacion> implements CatalogoEvaluacionFacadeLocal {


    public CatalogoEvaluacionFacade() {
        super(CatalogoEvaluacion.class);
    }

    @Override
    public List<CatalogoEvaluacion> buscarActivos() {
        try {
            Query query = em.createQuery("SELECT c FROM CatalogoEvaluacion c WHERE" + " (c.esActivo=TRUE)");
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }
    @Override
    public  CatalogoEvaluacion buscarPorCodigo(String codigo){
        List<CatalogoEvaluacion> catalogos = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT c FROM CatalogoEvaluacion c WHERE" + " (c.codigo=:codigo)");
            query.setParameter("codigo", codigo);
            catalogos = query.getResultList();
            return !catalogos.isEmpty() ? catalogos.get(0) : null;
        } catch (Exception e) {
        }
        return null;
    }
}