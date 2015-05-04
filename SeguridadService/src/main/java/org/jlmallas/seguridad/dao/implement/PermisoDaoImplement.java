/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.seguridad.dao.implement;

import org.jlmallas.seguridad.entity.Permiso;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.jlmallas.seguridad.dao.AbstractDao;
import org.jlmallas.seguridad.dao.PermisoDao;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class PermisoDaoImplement extends AbstractDao<Permiso> implements PermisoDao {

    public PermisoDaoImplement() {
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
