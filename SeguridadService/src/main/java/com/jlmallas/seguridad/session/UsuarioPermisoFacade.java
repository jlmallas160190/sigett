/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.seguridad.session;

import com.jlmallas.seguridad.entity.UsuarioPermiso;
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
public class UsuarioPermisoFacade extends AbstractFacade<UsuarioPermiso> implements UsuarioPermisoFacadeLocal {

    @PersistenceContext(unitName = "seguridadPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioPermisoFacade() {
        super(UsuarioPermiso.class);
    }

    public List<UsuarioPermiso> buscarPorUsuario(Long usuarioId) {
        try {
            Query query = em.createQuery("SELECT up from UsuarioPermiso up WHERE " + "(up.usuarioId.id=:id)");
            query.setParameter("id", usuarioId);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public UsuarioPermiso buscarPorUsuarioYCodigoPermiso(Long usuarioId, String codigoPermiso) {
        List<UsuarioPermiso> usuarioPermisos = new ArrayList<>();
        try {
            try {
                Query query = em.createQuery("SELECT up from UsuarioPermiso up WHERE " + "(up.permisoId.codigoNombre=:codigoNombre AND up.usuarioId.id=:id)");
                query.setParameter("id", usuarioId);
                query.setParameter("codigoNombre", codigoPermiso);
                usuarioPermisos = query.getResultList();
                return !usuarioPermisos.isEmpty() ? usuarioPermisos.get(0) : null;
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
        return null;
    }
}
