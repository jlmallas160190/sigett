/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.DocumentoActividad;
import java.util.ArrayList;
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
public class DocumentoActividadFacade extends AbstractFacade<DocumentoActividad> implements DocumentoActividadFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DocumentoActividadFacade() {
        super(DocumentoActividad.class);
    }

    @Override
    public List<DocumentoActividad> buscarPorActividad(Long actividadId) {
        try {
            Query query = em.createQuery("SELECT d FROM DocumentoActividad d WHERE" + " (d.actividadId.id=:id)");
            query.setParameter("id", actividadId);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public DocumentoActividad documentoActual(Long actividadId) {
        List<DocumentoActividad> documentoActividads = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT d FROM DocumentoActividad d WHERE" + " (d.actividadId.id=:id AND d.esActual=TRUE)");
            query.setParameter("id", actividadId);
            documentoActividads = query.getResultList();
            return !documentoActividads.isEmpty() ? documentoActividads.get(0) : null;
        } catch (Exception e) {
        }
        return null;
    }
}
