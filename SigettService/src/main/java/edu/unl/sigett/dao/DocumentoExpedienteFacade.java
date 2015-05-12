/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.DocumentoExpediente;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class DocumentoExpedienteFacade extends AbstractDao<DocumentoExpediente> implements DocumentoExpedienteFacadeLocal {

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
