/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao.implement;

import edu.jlmallas.academico.dao.AbstractDao;
import edu.jlmallas.academico.dao.PeriodoAcademicoDao;
import edu.jlmallas.academico.entity.PeriodoAcademico;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class PeriodoAcademicoDaoImplement extends AbstractDao<PeriodoAcademico> implements PeriodoAcademicoDao {

    public PeriodoAcademicoDaoImplement() {
        super(PeriodoAcademico.class);
    }

    @Override
    public List<PeriodoAcademico> buscarPorCriterio(PeriodoAcademico periodoAcademico) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("Select p from PeriodoAcademico p where 1=1 ");
        if (periodoAcademico.getIdSga() != null) {
            sql.append(" and p.idSga=:idSga ");
            parametros.put("idSga", periodoAcademico.getIdSga());
        }
        sql.append(" order by p.fechaInicio asc ");
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }

    @Override
    public PeriodoAcademico buscarPorIdSga(PeriodoAcademico periodoAcademico) {
        StringBuilder sql = new StringBuilder();
        @SuppressWarnings("UnusedAssignment")
        List<PeriodoAcademico> periodoAcademicos = new ArrayList<>();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("Select p from PeriodoAcademico p where p.idSga=:idSga ");
        parametros.put("idSga", periodoAcademico.getIdSga());
        sql.append(" order by p.fechaInicio asc ");
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        periodoAcademicos = q.getResultList();
        return !periodoAcademicos.isEmpty() ? periodoAcademicos.get(0) : null;
    }

}
