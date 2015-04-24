/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.seguridad.session;

import com.jlmallas.seguridad.entity.Rol;
import com.jlmallas.seguridad.session.AbstractFacade;
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
public class RolFacade extends AbstractFacade<Rol> implements RolFacadeLocal {

    @PersistenceContext(unitName = "seguridadPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RolFacade() {
        super(Rol.class);
    }

    public List<Rol> buscarPorNombre(String nombre) {
        List<Rol> roles = new ArrayList<>();
        try {
            Query query = em.createQuery("Select rol from Rol rol where " + " (LOWER(rol.nombre) like concat('%',LOWER(:nombre),'%'))");
            query.setParameter("nombre", nombre);
            roles = query.getResultList();
        } catch (Exception e) {
        }
        return roles;
    }
}
