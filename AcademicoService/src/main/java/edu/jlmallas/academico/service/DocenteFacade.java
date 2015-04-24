/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.dao.AbstractFacade;
import edu.jlmallas.academico.entity.Docente;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class DocenteFacade extends AbstractFacade<Docente> implements DocenteFacadeLocal {


    public DocenteFacade() {
        super(Docente.class);
    }

    @Override
    public List<Docente> buscarPorCriterio(String criterio) {
        try {
            Query query = em.createQuery("Select d from Docente d where " + "(LOWER(d.persona.apellidos) like concat('%',LOWER(:criterio),'%')"
                    + " or LOWER(d.persona.nombres) like concat('%',LOWER(:criterio),'%')) order by d.persona.apellidos");
            query.setParameter("criterio", criterio);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<Docente> buscarDocentesDisponiblesMiembrosTribunal(Long proyectoId) {
        try {
            StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery("docentesDisponiblesMiembrosTribunal");
            storedProcedureQuery.setParameter("proyectoId", proyectoId);
            storedProcedureQuery.execute();
            List<Docente> result = (List<Docente>) storedProcedureQuery.getResultList();
            return result;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}
