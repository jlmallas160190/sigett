/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.InformeProyecto;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class InformeProyectoFacade extends AbstractDao<InformeProyecto> implements InformeProyectoFacadeLocal {

    public InformeProyectoFacade() {
        super(InformeProyecto.class);
    }

    @Override
    public List<InformeProyecto> buscarPorProyecto(Long proyectoId) {
        try {
            Query query = em.createQuery("Select i from InformeProyecto i WHERE " + "(i.proyectoId.id=:id)");
            query.setParameter("id", proyectoId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
