/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service.implement;

import edu.jlmallas.academico.entity.PeriodoCoordinacion;
import edu.jlmallas.academico.dao.AbstractFacade;
import edu.jlmallas.academico.service.PeriodoCoordinacionFacadeLocal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class PeriodoCoordinacionServiceImplement extends AbstractFacade<PeriodoCoordinacion> implements PeriodoCoordinacionFacadeLocal {

    public PeriodoCoordinacionServiceImplement() {
        super(PeriodoCoordinacion.class);
    }

    @Override
    public List<PeriodoCoordinacion> buscarPorCarrera(Integer carreraId) {
        try {
            Query query = em.createQuery("SELECT p from PeriodoCoordinacion p WHERE " + "(p.carreraId.id=:id)");
            query.setParameter("id", carreraId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<PeriodoCoordinacion> buscarActivos() {
        try {
            Query query = em.createQuery("SELECT p from PeriodoCoordinacion p WHERE " + "(p.esActivo=TRUE)");
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
