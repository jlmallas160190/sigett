/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.TemaProyecto;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class TemaProyectoFacade extends AbstractDao<TemaProyecto> implements TemaProyectoFacadeLocal {

    public TemaProyectoFacade() {
        super(TemaProyecto.class);
    }

    @Override
    public List<TemaProyecto> buscarPorProyecto(Long proyectoId) {
        try {
            Query query = em.createQuery("SELECT t FROM TemaProyecto t WHERE " + "(t.proyectoId.id=:id)");
            query.setParameter("id", proyectoId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
