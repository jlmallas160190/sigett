/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao.implement;

import edu.jlmallas.academico.dao.AbstractDao;
import edu.jlmallas.academico.dao.OfertaAcademicaDao;
import edu.jlmallas.academico.entity.OfertaAcademica;
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
public class OfertaAcademicaDaoImplement extends AbstractDao<OfertaAcademica> implements OfertaAcademicaDao {

    public OfertaAcademicaDaoImplement() {
        super(OfertaAcademica.class);
    }

    @Override
    public OfertaAcademica buscarPorIdSga(String id) {
        List<OfertaAcademica> ofertaAcademicas = new ArrayList<>();
        try {
            Query query = em.createNamedQuery("OfertaAcademica.findByIdSga");
            query.setParameter("idSga", id);
            ofertaAcademicas = query.getResultList();
            return !ofertaAcademicas.isEmpty() ? ofertaAcademicas.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<OfertaAcademica> buscarPorPeriodoActual() {
        try {
            Query query = em.createQuery("SELECT o from OfertaAcademica o WHERE " + "(o.periodoAcademicoId.idSga=(SELECT MAX(p.idSga) from PeriodoAcademico p)) order by o.idSga DESC");
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public OfertaAcademica ultimaOfertaPorFechaYPeriodoLectivo(Integer periodoId) {
        List<OfertaAcademica> ofertaAcademicas = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT o from OfertaAcademica o WHERE " + " o.fechaFin= (SELECT MAX(o1.fechaFin) FROM OfertaAcademica o1 "
                    + "WHERE o1.periodoAcademicoId.id=:id)");
            query.setParameter("id", periodoId);
            ofertaAcademicas = query.getResultList();
            return !ofertaAcademicas.isEmpty() ? ofertaAcademicas.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public OfertaAcademica primerOfertaPorFechaYPeriodoLectivo(Integer periodoId) {
        List<OfertaAcademica> ofertaAcademicas = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT o from OfertaAcademica o WHERE " + " o.fechaInicio= (SELECT MIN(o1.fechaInicio) "
                    + "FROM OfertaAcademica o1 WHERE o1.periodoAcademicoId.id=:id)");
            query.setParameter("id", periodoId);
            ofertaAcademicas = query.getResultList();
            return !ofertaAcademicas.isEmpty() ? ofertaAcademicas.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<OfertaAcademica> buscar(final OfertaAcademica ofertaAcademica) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT o from OfertaAcademica o WHERE 1=1 ");
        if (ofertaAcademica.getIdSga() != null) {
            sql.append(" and o.idSga = :idSga ");
            parametros.put("idSga", ofertaAcademica.getIdSga());
            existeFiltro = Boolean.TRUE;
        }
        if (ofertaAcademica.getPeriodoAcademicoId() != null) {
            sql.append(" and o.periodoId=:periodoId");
            parametros.put("periodoId", ofertaAcademica.getPeriodoAcademicoId());
            existeFiltro = Boolean.TRUE;
        }
        if (!existeFiltro) {
            return new ArrayList<>();
        }
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }

}
