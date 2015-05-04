/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.seguridad.dao.implement;

import org.jlmallas.seguridad.entity.Rol;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.jlmallas.seguridad.dao.AbstractDao;
import org.jlmallas.seguridad.dao.RolDao;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class RolDaoImplement extends AbstractDao<Rol> implements RolDao {

    public RolDaoImplement() {
        super(Rol.class);
    }

    @Override
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
