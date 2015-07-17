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
 * @author jorge-luis
 */
@Local
public interface ReporteMatriculaService {

    void guardar(final ReporteMatricula reporteMatricula);

    void actualizar(final ReporteMatricula reporteMatricula);

    void eliminar(final ReporteMatricula reporteMatricula);

    ReporteMatricula buscarUltimaMatricula(final ReporteMatricula reporteMatricula);

    ReporteMatricula buscarPrimerMatricula(final ReporteMatricula reporteMatricula);

    ReporteMatricula buscarPorMatriculaId(final ReporteMatricula reporteMatricula);

    List<ReporteMatricula> buscar(final ReporteMatricula reporteMatricula);
}
