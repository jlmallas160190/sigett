/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.seguridad.dao.implement;

import org.jlmallas.seguridad.entity.RolUsuario;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.jlmallas.seguridad.dao.AbstractDao;
import org.jlmallas.seguridad.dao.RolUsuarioDao;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class RolUsuarioDaoImplement extends AbstractDao<RolUsuario> implements RolUsuarioDao {

    public RolUsuarioDaoImplement() {
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
