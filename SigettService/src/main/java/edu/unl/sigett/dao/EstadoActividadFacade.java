/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.EstadoActividad;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class EstadoActividadFacade extends AbstractDao<EstadoActividad> implements EstadoActividadFacadeLocal {

    public EstadoActividadFacade() {
        super(EstadoActividad.class);
    }

    @Override
    public List<EstadoActividad> buscarActivos() {
        try {
            Query query = em.createQuery("SELECT e FROM EstadoActividad e WHERE " + "(e.esActivo=TRUE)");
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public List<EstadoActividad> buscarPorNombre(String nombre) {
        try {
            Query query = em.createNamedQuery("EstadoActividad.findByNombre");
            query.setParameter("nombre", nombre);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }
}