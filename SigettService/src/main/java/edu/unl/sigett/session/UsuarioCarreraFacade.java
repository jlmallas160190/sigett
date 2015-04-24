/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.UsuarioCarrera;
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
public class UsuarioCarreraFacade extends AbstractFacade<UsuarioCarrera> implements UsuarioCarreraFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioCarreraFacade() {
        super(UsuarioCarrera.class);
    }

    @Override
    public List<UsuarioCarrera> buscarPorUsuario(Long usuarioId) {
        try {
            Query query = em.createQuery("SELECT uc from UsuarioCarrera uc WHERE "+ "(uc.usuarioId=:id)");
            query.setParameter("id", usuarioId);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
