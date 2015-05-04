/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.seguridad.dao.implement;

import org.jlmallas.seguridad.entity.RolPermiso;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.jlmallas.seguridad.dao.AbstractDao;
import org.jlmallas.seguridad.dao.RolPermisoDao;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class RolPermisoDaoImplement extends AbstractDao<RolPermiso> implements RolPermisoDao {

    public RolPermisoDaoImplement() {
        super(RolPermiso.class);
    }

    @Override
    public List<RolPermiso> buscarPorRol(Long rolId) {
        try {
            Query query = em.createQuery("SELECT rp from RolPermiso rp WHERE " + "(rp.rolId.id=:id)");
            query.setParameter("id", rolId);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public RolPermiso buscarPorRolCodigoPermiso(Long rolId, String codigoNombre) {
        List<RolPermiso> rolesPermisos = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT rp from RolPermiso rp WHERE " + "(rp.permisoId.codigoNombre=:codigoNombre AND rp.rolId.id=:id)");
            query.setParameter("id", rolId);
            query.setParameter("codigoNombre", codigoNombre);
            rolesPermisos=query.getResultList();
            return !rolesPermisos.isEmpty() ? rolesPermisos.get(0) : null;
        } catch (Exception e) {
        }
        return null;
    }
}
