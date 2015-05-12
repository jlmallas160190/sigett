/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao.implement;

import edu.jlmallas.academico.dao.AbstractDao;
import edu.jlmallas.academico.entity.Docente;
import edu.jlmallas.academico.dao.DocenteDao;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class DocenteDaoImplement extends AbstractDao<Docente> implements DocenteDao {

    public DocenteDaoImplement() {
        super(Docente.class);
    }

    @Override
    public List<Docente> buscarPorCriterio(Docente docente) {
        try {
            StringBuilder sql = new StringBuilder();
            HashMap<String, Object> parametros = new HashMap<String, Object>();
            sql.append("Select d from Docente d where 1=1 ");
            final Query q = em.createQuery(sql.toString());
            for (String key : parametros.keySet()) {
                q.setParameter(key, parametros.get(key));
            }
            return q.getResultList();
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
