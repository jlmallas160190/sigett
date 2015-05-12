/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.Prorroga;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class ProrrogaFacade extends AbstractDao<Prorroga> implements ProrrogaFacadeLocal {


    public ProrrogaFacade() {
        super(Prorroga.class);
    }

    @Override
    public List<Prorroga> buscarPorProyecto(Long cronogramaId) {
        try {
            Query query = em.createQuery("SELECT p FROM Prorroga p WHERE " + "(p.cronogramaId.id=:id)");
            query.setParameter("id", cronogramaId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}