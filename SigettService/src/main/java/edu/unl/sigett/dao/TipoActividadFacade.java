/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.TipoActividad;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class TipoActividadFacade extends AbstractDao<TipoActividad> implements TipoActividadFacadeLocal {

    public TipoActividadFacade() {
        super(TipoActividad.class);
    }

    @Override
    public List<TipoActividad> buscarActivos() {
        try {
            Query query = em.createQuery("SELECT t FROM TipoActividad t WHERE " + "(t.esActivo=TRUE)");
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public List<TipoActividad> buscarPorNombre(String nombre) {
        try {
            Query query = em.createNamedQuery("TipoActividad.findByNombre");
            query.setParameter("nombre", nombre);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }
}
