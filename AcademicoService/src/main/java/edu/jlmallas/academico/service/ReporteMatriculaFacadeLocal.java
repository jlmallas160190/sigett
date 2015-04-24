/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.entity.ReporteMatricula;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface ReporteMatriculaFacadeLocal {

    void create(ReporteMatricula reporteMatricula);

    void edit(ReporteMatricula reporteMatricula);

    void remove(ReporteMatricula reporteMatricula);

    ReporteMatricula find(Object id);

    List<ReporteMatricula> findAll();

    List<ReporteMatricula> findRange(int[] range);

    ReporteMatricula buscarPorMatriculaId(Long matriculaId);

    List<ReporteMatricula> buscarPorEstudianteCarrera(Long estudianteCarreraId);

    ReporteMatricula buscarUltimaMatriculaEstudiante(Long estudianteId);

    ReporteMatricula buscarPrimerMatriculaEstudiante(Long estudianteId);

    int count();

}
