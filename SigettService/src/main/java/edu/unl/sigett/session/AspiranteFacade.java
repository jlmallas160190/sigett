/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.Aspirante;
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
public class AspiranteFacade extends AbstractFacade<Aspirante> implements AspiranteFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AspiranteFacade() {
        super(Aspirante.class);
    }

    @Override
    public List<Aspirante> aptos(Integer carreraId) {
        try {
            Query query = em.createQuery("SELECT a from Aspirante a WHERE" + " (a.esApto=TRUE AND a.estudianteCarrera.carreraId.id=:id)");
            query.setParameter("id", carreraId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<Aspirante> buscarPorCarrera(Integer carreraId) {
        try {
            Query query = em.createQuery("SELECT a from  Aspirante a WHERE " + " (a.estudianteCarrera.carreraId.id=:id AND a.estudianteCarrera.esActivo=TRUE AND a.estudianteCarrera.estadoEstudianteCarrera.id != 3)");
            query.setParameter("id", carreraId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
