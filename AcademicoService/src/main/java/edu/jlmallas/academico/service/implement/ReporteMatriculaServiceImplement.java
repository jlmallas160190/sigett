/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service.implement;

import edu.jlmallas.academico.service.ReporteMatriculaService;
import edu.jlmallas.academico.dao.ReporteMatriculaDao;
import edu.jlmallas.academico.entity.ReporteMatricula;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class ReporteMatriculaServiceImplement implements ReporteMatriculaService {

    @EJB
    private ReporteMatriculaDao reporteMatriculaDao;

    @Override
    public void guardar(final ReporteMatricula reporteMatricula) {
        this.reporteMatriculaDao.create(reporteMatricula);
    }

    @Override
    public void actualizar(final ReporteMatricula reporteMatricula) {
        this.reporteMatriculaDao.edit(reporteMatricula);
    }

    @Override
    public void eliminar(final ReporteMatricula reporteMatricula) {
        this.reporteMatriculaDao.remove(reporteMatricula);
    }

    @Override
    public ReporteMatricula buscarUltimaMatricula(ReporteMatricula reporteMatricula) {
        return this.reporteMatriculaDao.buscarUltimaMatriculaEstudiante(reporteMatricula.getEstudianteCarreraId().getId());
    }

    @Override
    public ReporteMatricula buscarPrimerMatricula(final ReporteMatricula reporteMatricula) {
        return this.reporteMatriculaDao.buscarPrimerMatriculaEstudiante(reporteMatricula.getEstudianteCarreraId().getId());
    }

    @Override
    public ReporteMatricula buscarPorMatriculaId(ReporteMatricula reporteMatricula) {
        List<ReporteMatricula> reporteMatriculas = this.reporteMatriculaDao.buscar(reporteMatricula);
        if (reporteMatriculas == null) {
            return null;
        }
        return !reporteMatriculas.isEmpty() ? reporteMatriculas.get(0) : null;
    }

    @Override
    public List<ReporteMatricula> buscar(ReporteMatricula reporteMatricula) {
        List<ReporteMatricula> reporteMatriculas = this.reporteMatriculaDao.buscar(reporteMatricula);
        if (reporteMatriculas == null) {
            return null;
        }
        return reporteMatriculas;
    }

}
