/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.ConfiguracionArea;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class ConfiguracionAreaFacade extends AbstractFacade<ConfiguracionArea> implements ConfiguracionAreaFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConfiguracionAreaFacade() {
        super(ConfiguracionArea.class);
    }

    @Override
    public ConfiguracionArea buscarPorAreaId(Integer areaId, String codigo) {
        List<ConfiguracionArea> configuracionArea = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT c FROM  ConfiguracionArea c WHERE" + " (c.areaId.id=:areaId AND c.codigo=:codigo)");
            query.setParameter("areaId", areaId);
            query.setParameter("codigo", codigo);
            configuracionArea = query.getResultList();
            return !configuracionArea.isEmpty() ? configuracionArea.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
