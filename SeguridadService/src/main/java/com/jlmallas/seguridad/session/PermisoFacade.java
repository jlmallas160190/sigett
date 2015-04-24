/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.seguridad.session;

import com.jlmallas.seguridad.entity.Permiso;
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
public class PermisoFacade extends AbstractFacade<Permiso> implements PermisoFacadeLocal {

    @PersistenceContext(unitName = "seguridadPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PermisoFacade() {
        super(Permiso.class);
    }

    @Override
    public List<Permiso> buscarPorNombre(String nombre) {
        List<Permiso> permisos = new ArrayList<>();
        try {
            Query query = em.createQuery("Select p from Permiso p WHERE" + " ( p.nombre like concat('%',:criterio,'%') )");
            query.setParameter("criterio", nombre);
            permisos = query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return permisos;
    }

}
