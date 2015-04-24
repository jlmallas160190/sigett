/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.soporte.session;

import com.jlmallas.soporte.entity.Objeto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class ObjetoFacade extends AbstractFacade<Objeto> implements ObjetoFacadeLocal {

    @PersistenceContext(unitName = "soportePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ObjetoFacade() {
        super(Objeto.class);
    }

    @Override
    public Objeto buscarPorNombre(String nombre) {
        List<Objeto> objetos = new ArrayList<>();
        try {
            Query query = em.createNamedQuery("Objeto.findByNombre");
            query.setParameter("nombre", nombre);
            objetos = query.getResultList();
            return !objetos.isEmpty() ? objetos.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<Objeto> buscarPorOrdenAlfabetico() {
        try {
            Query query = em.createQuery("SELECT o FROM Objeto o  ORDER BY o.nombre");
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }
}
