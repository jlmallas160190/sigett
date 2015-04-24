/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.ConfiguracionCarrera;
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
public class ConfiguracionCarreraFacade extends AbstractFacade<ConfiguracionCarrera> implements ConfiguracionCarreraFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConfiguracionCarreraFacade() {
        super(ConfiguracionCarrera.class);
    }

    @Override
    public ConfiguracionCarrera buscarPorCarreraId(Integer carreraId, String codigo) {
        List<ConfiguracionCarrera> configuracionCarreras = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT c FROM  ConfiguracionCarrera c WHERE" + " (c.carreraId.id=:carreraId AND c.codigo=:codigo)");
            query.setParameter("carreraId", carreraId);
            query.setParameter("codigo", codigo);
            configuracionCarreras = query.getResultList();
            return !configuracionCarreras.isEmpty() ? configuracionCarreras.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
