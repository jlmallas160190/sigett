/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.RangoEquivalencia;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class RangoEquivalenciaFacade extends AbstractDao<RangoEquivalencia> implements RangoEquivalenciaFacadeLocal {


    public RangoEquivalenciaFacade() {
        super(RangoEquivalencia.class);
    }

    @Override
    public List<RangoEquivalencia> buscarPorRangoNota(Integer id) {
        try {
            Query query = em.createQuery("SELECT r FROM RangoEquivalencia r WHERE " + "(r.rangoNotaId.id=:id)");
            query.setParameter("id", id);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }
}
