/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.Miembro;
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
public class MiembroFacade extends AbstractFacade<Miembro> implements MiembroFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MiembroFacade() {
        super(Miembro.class);
    }

    @Override
    public List<Miembro> buscarPorTribunal(Long tribunalId) {
        try {
            Query query = em.createQuery("SELECT m FROM Miembro m WHERE" + " (m.tribunalId.id=:id and m.esActivo=TRUE)");
            query.setParameter("id", tribunalId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<Miembro> buscarPorDocente(Long docenteId) {
        try {
            Query query = em.createQuery("SELECT m FROM Miembro m WHERE" + " (m.docenteId.id=:id and m.esActivo=TRUE) order by m.id DESC");
            query.setParameter("id", docenteId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
