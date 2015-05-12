/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.CatalogoDocumentoExpediente;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class CatalogoDocumentoExpedienteFacade extends AbstractDao<CatalogoDocumentoExpediente> implements CatalogoDocumentoExpedienteFacadeLocal {


    public CatalogoDocumentoExpedienteFacade() {
        super(CatalogoDocumentoExpediente.class);
    }

    public List<CatalogoDocumentoExpediente> buscar(String criterio) {
        try {
            Query query = em.createNamedQuery("CatalogoDocumentoExpediente.findByNombre");
            query.setParameter("nombre", criterio);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<CatalogoDocumentoExpediente> buscarActivos() {
        try {
            Query query = em.createQuery("SELECT c FROM CatalogoDocumentoExpediente c WHERE" + " (c.esActivo=TRUE)");
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<CatalogoDocumentoExpediente> buscarObligatorios() {
        try {
            Query query = em.createQuery("SELECT c FROM CatalogoDocumentoExpediente c WHERE" + " (c.esObligatorio=TRUE)");
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
