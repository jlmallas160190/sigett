/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao.implement;

import edu.jlmallas.academico.dao.AbstractDao;
import edu.jlmallas.academico.dao.ReporteMatriculaDao;
import edu.jlmallas.academico.entity.ReporteMatricula;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class ReporteMatriculaDaoImplement extends AbstractDao<ReporteMatricula> implements ReporteMatriculaDao {

    public ReporteMatriculaDaoImplement() {
        super(ReporteMatricula.class);
    }

    @Override
    public ReporteMatricula buscarUltimaMatriculaEstudiante(Long estudianteCarreraId) {
        @SuppressWarnings("UnusedAssignment")
        List<ReporteMatricula> reporteMatriculas = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT rm  from ReporteMatricula rm WHERE " + " rm.matriculaId= (SELECT MAX(rm1.matriculaId) "
                    + "FROM ReporteMatricula rm1 WHERE rm1.estudianteCarreraId.id=:id) ");
            query.setParameter("id", estudianteCarreraId);
            reporteMatriculas = query.getResultList();
            return !reporteMatriculas.isEmpty() ? reporteMatriculas.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public ReporteMatricula buscarPrimerMatriculaEstudiante(Long estudianteCarreraId) {
        List<ReporteMatricula> reporteMatriculas = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT rm  from ReporteMatricula rm WHERE " + " rm.matriculaId= (SELECT MIN(rm1.matriculaId) FROM "
                    + "ReporteMatricula rm1 WHERE rm1.estudianteCarreraId.id=:id)");
            query.setParameter("id", estudianteCarreraId);
            reporteMatriculas = query.getResultList();
            return !reporteMatriculas.isEmpty() ? reporteMatriculas.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<ReporteMatricula> buscar(final ReporteMatricula reporteMatricula) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT rm  from ReporteMatricula rm WHERE 1=1 ");
        if (reporteMatricula.getMatriculaId() != null) {
            sql.append(" and rm.matriculaId=:matriculaId");
            parametros.put("matriculaId", reporteMatricula.getMatriculaId());
            existeFiltro = Boolean.TRUE;
        }
        if (reporteMatricula.getEstudianteCarreraId() != null) {
            sql.append(" and rm.estudianteCarreraId=:estudianteCarreraId");
            parametros.put("estudianteCarreraId", reporteMatricula.getEstudianteCarreraId());
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
