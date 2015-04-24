/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.dao.AbstractFacade;
import edu.jlmallas.academico.entity.Titulo;
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
public class TituloFacade extends AbstractFacade<Titulo> implements TituloFacadeLocal {

    public TituloFacade() {
        super(Titulo.class);
    }

    @Override
    public Titulo buscarPorNombre(String nombre) {
        List<Titulo> titulos = new ArrayList<>();
        try {
            Query query = em.createNamedQuery("Titulo.findByNombre");
            query.setParameter("nombre", nombre);
            titulos = query.getResultList();
            return !titulos.isEmpty() ? titulos.get(0) : null;
        } catch (Exception e) {
        }
        return null;
    }
}
