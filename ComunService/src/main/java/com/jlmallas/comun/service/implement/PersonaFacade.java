/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.service.implement;

import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.service.AbstractFacade;
import com.jlmallas.comun.service.PersonaFacadeLocal;
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
public class PersonaFacade extends AbstractFacade<Persona> implements PersonaFacadeLocal {

    @PersistenceContext(unitName = "comunPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PersonaFacade() {
        super(Persona.class);
    }

    @Override
    public boolean esUnico(String numeroIdentificacion, Long id) {
        boolean var = false;
        Persona per = null;
        if (id != null) {
            per = find(id);
        }
        Persona p = buscarPorNumeroIdentificacion(numeroIdentificacion);
        if (p != null) {
            if (per != null) {
                if (per.getId() != null) {
                    if (p == per) {
                        var = true;
                    } else {
                        var = false;
                    }
                } else {
                    var = false;
                }
            }
        } else {
            var = true;
        }
        return var;
    }

    @Override
    public Persona buscarPorNumeroIdentificacion(String numeroIdentificacion) {
        List<Persona> personas = new ArrayList<>();
        try {
            Query query = em.createNamedQuery("Persona.findByNumeroIdentificacion");
            query.setParameter("numeroIdentificacion", numeroIdentificacion);
            personas = query.getResultList();
            return !personas.isEmpty() ? personas.get(0) : null;
        } catch (Exception e) {
        }
        return null;
    }
}
