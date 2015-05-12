/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.OficioCarrera;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class OficioCarreraFacade extends AbstractDao<OficioCarrera> implements OficioCarreraFacadeLocal {

    public OficioCarreraFacade() {
        super(OficioCarrera.class);
    }

    @Override
    public List<OficioCarrera> buscarPorCarrera(Integer carreraId) {
        try {
            Query query = em.createQuery("SELECT o FROM OficioCarrera o WHERE" + " (o.carreraId.id=:id)");
            query.setParameter("id", carreraId);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public OficioCarrera buscarPorTablaId(Long id, String catalogoCodigo) {
        List<OficioCarrera> oficioCarreras = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT o FROM OficioCarrera o WHERE" + " (o.tablaOficioId=:id and o.catalogoOficioId.codigo=:catalogoCodigo)");
            query.setParameter("id", id);
            query.setParameter("catalogoCodigo", catalogoCodigo);
            oficioCarreras = query.getResultList();
            return !oficioCarreras.isEmpty() ? oficioCarreras.get(0) : null;
        } catch (Exception e) {
        }
        return null;
    }
}
