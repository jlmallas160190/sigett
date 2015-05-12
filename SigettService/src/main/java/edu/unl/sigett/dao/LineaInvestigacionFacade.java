/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.LineaInvestigacion;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class LineaInvestigacionFacade extends AbstractDao<LineaInvestigacion> implements LineaInvestigacionFacadeLocal {

    public LineaInvestigacionFacade() {
        super(LineaInvestigacion.class);
    }

    @Override
    public List<LineaInvestigacion> buscarPorCriterio(String nombre) {
        try {
            Query query = em.createQuery("SELECT l FROM LineaInvestigacion l WHERE " + " (LOWER(l.nombre) like concat('%',LOWER(:nombre),'%'))");
            query.setParameter("nombre", nombre);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }
}
