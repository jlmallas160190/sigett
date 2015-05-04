/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.seguridad.dao.implement;

import org.jlmallas.seguridad.entity.UsuarioPermiso;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.jlmallas.seguridad.dao.AbstractDao;
import org.jlmallas.seguridad.dao.UsuarioPermisoDao;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class UsuarioPermisoDaoImplement extends AbstractDao<UsuarioPermiso> implements UsuarioPermisoDao {

    public UsuarioPermisoDaoImplement() {
        super(UsuarioPermiso.class);
    }

    @Override
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
