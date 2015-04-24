/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.LineaInvestigacionDocente;
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
public class LineaInvestigacionDocenteFacade extends AbstractFacade<LineaInvestigacionDocente> implements LineaInvestigacionDocenteFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LineaInvestigacionDocenteFacade() {
        super(LineaInvestigacionDocente.class);
    }

    @Override
    public List<LineaInvestigacionDocente> buscarPorDocenteCi(String numeroIdentificacion) {
        try {
            Query query = em.createQuery("SELECT ld FROM LineaInvestigacionDocente ld WHERE " + "(ld.docenteId.persona.numeroIdentificacion=:numeroIdentificacion)");
            query.setParameter("numeroIdentificacion", numeroIdentificacion);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public List<LineaInvestigacionDocente> buscarPorDocenteId(Long docenteId) {
        try {
            Query query = em.createQuery("SELECT ld FROM LineaInvestigacionDocente ld WHERE " + "(ld.docenteId.id=:id)");
            query.setParameter("id", docenteId);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }
}
