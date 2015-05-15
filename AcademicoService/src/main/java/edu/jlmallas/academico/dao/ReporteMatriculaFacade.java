/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao;

import edu.jlmallas.academico.dao.implement.ReporteMatriculaFacadeLocal;
import edu.jlmallas.academico.dao.AbstractDao;
import edu.jlmallas.academico.entity.ReporteMatricula;
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
public class ReporteMatriculaFacade extends AbstractDao<ReporteMatricula> implements ReporteMatriculaFacadeLocal {


    public ReporteMatriculaFacade() {
        super(ReporteMatricula.class);
    }

    public ReporteMatricula buscarPorMatriculaId(Long matriculaId) {
        List<ReporteMatricula> reporteMatriculas = new ArrayList<>();
        try {
            Query query = em.createNamedQuery("ReporteMatricula.findByMatriculaId");
            query.setParameter("matriculaId", matriculaId);
            reporteMatriculas = query.getResultList();
            return !reporteMatriculas.isEmpty() ? reporteMatriculas.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<ReporteMatricula> buscarPorEstudianteCarrera(Long estudianteCarreraId) {
        try {
            Query query = em.createQuery("SELECT rm  from ReporteMatricula rm WHERE " + "(rm.estudianteCarrera.id=:id)");
            query.setParameter("id", estudianteCarreraId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public ReporteMatricula buscarUltimaMatriculaEstudiante(Long estudianteCarreraId) {
        List<ReporteMatricula> reporteMatriculas = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT rm  from ReporteMatricula rm WHERE " + " rm.matriculaId= (SELECT MAX(rm1.matriculaId) FROM ReporteMatricula rm1 WHERE rm1.estudianteCarrera.id=:id) ");
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
            Query query = em.createQuery("SELECT rm  from ReporteMatricula rm WHERE " + " rm.matriculaId= (SELECT MIN(rm1.matriculaId) FROM ReporteMatricula rm1 WHERE rm1.estudianteCarrera.id=:id)");
            query.setParameter("id", estudianteCarreraId);
            reporteMatriculas = query.getResultList();
            return !reporteMatriculas.isEmpty() ? reporteMatriculas.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
