/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.dao.AbstractFacade;
import edu.jlmallas.academico.entity.Nivel;
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
public class NivelFacade extends AbstractFacade<Nivel> implements NivelFacadeLocal {

    public NivelFacade() {
        super(Nivel.class);
    }

    public Nivel buscarPorNombre(String nombre) {
        List<Nivel> niveles = new ArrayList<>();
        try {
            Query query = em.createNamedQuery("Nivel.findByNombre");
            query.setParameter("nombre", nombre);
            niveles = query.getResultList();
            return !niveles.isEmpty() ? niveles.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
