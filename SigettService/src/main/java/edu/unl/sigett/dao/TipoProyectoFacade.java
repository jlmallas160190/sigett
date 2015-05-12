/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.TipoProyecto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class TipoProyectoFacade extends AbstractDao<TipoProyecto> implements TipoProyectoFacadeLocal {

    public TipoProyectoFacade() {
        super(TipoProyecto.class);
    }

    @Override
    public List<TipoProyecto> buscarActivos() {
        try {
            Query query = em.createQuery("Select t from TipoProyecto t where t.esActivo=TRUE ");
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    @Override
    public List<TipoProyecto> buscarPorCriterio(String criterio) {
        try {
            Query query = em.createQuery("Select t from TipoProyecto t WHERE " + " (t.nombre like concat('%',:criterio,'%'))");
            query.setParameter("criterio", criterio);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    @Override
    public TipoProyecto buscarPorCodigo(String codigo) {
        List<TipoProyecto> tipos = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT t FROM TipoProyecto t WHERE" + " (t.codigo=:codigo)");
            query.setParameter("codigo", codigo);
            tipos = query.getResultList();
            return !tipos.isEmpty() ? tipos.get(0) : null;
        } catch (Exception e) {
        }
        return null;
    }
}
