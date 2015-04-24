/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.dao.AbstractFacade;
import edu.jlmallas.academico.entity.TipoContrato;
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
public class TipoContratoFacade extends AbstractFacade<TipoContrato> implements TipoContratoFacadeLocal {

    public TipoContratoFacade() {
        super(TipoContrato.class);
    }

    @Override
    public TipoContrato buscarPorNombre(String nombre) {
        List<TipoContrato> tipoContratos = new ArrayList<>();
        try {
            Query query = em.createNamedQuery("TipoContrato.findByNombre");
            query.setParameter("nombre", nombre);
            tipoContratos = query.getResultList();
            return !tipoContratos.isEmpty() ? tipoContratos.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
