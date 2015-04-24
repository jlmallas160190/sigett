/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.seguridad.session;

import com.jlmallas.seguridad.entity.RolUsuario;
import com.jlmallas.seguridad.session.AbstractFacade;
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
public class RolUsuarioFacade extends AbstractFacade<RolUsuario> implements RolUsuarioFacadeLocal {

    @PersistenceContext(unitName = "seguridadPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RolUsuarioFacade() {
        super(RolUsuario.class);
    }

    @Override
    public List<RolUsuario> buscarPorUsuario(Long usuarioId) {
        try {
            Query query = em.createQuery("SELECT ru from RolUsuario ru WHERE " + "(ru.usuarioId.id=:id)");
            query.setParameter("id", usuarioId);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }
}
