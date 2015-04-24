/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.DocumentoExpediente;
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
public class DocumentoExpedienteFacade extends AbstractFacade<DocumentoExpediente> implements DocumentoExpedienteFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DocumentoExpedienteFacade() {
        super(DocumentoExpediente.class);
    }

    @Override
    public List<DocumentoExpediente> buscarPorExpediente(Long expedienteId) {
        try {
            Query query = em.createQuery("SELECT de FROM DocumentoExpediente de WHERE" + " (de.expedienteId.id=:id)");
            query.setParameter("id", expedienteId);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }
}
