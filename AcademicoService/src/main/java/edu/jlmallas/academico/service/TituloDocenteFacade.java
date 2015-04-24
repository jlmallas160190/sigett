/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.dao.AbstractFacade;
import edu.jlmallas.academico.entity.TituloDocente;
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
public class TituloDocenteFacade extends AbstractFacade<TituloDocente> implements TituloDocenteFacadeLocal {


    public TituloDocenteFacade() {
        super(TituloDocente.class);
    }

    @Override
    public TituloDocente buscarPorTitulo(String nombre) {
        List<TituloDocente> tituloDocentes = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT td FROM TituloDocente td WHERE" + " (td.tituloId.nombre=:nombre)");
            query.setParameter("nombre", nombre);
            tituloDocentes = query.getResultList();
            return !tituloDocentes.isEmpty() ? tituloDocentes.get(0) : null;
        } catch (Exception e) {
        }
        return null;
    }
}
