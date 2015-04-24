/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.entity.PeriodoAcademico;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface PeriodoAcademicoService {

    void guardar(PeriodoAcademico periodoAcademico);

    void actualizar(PeriodoAcademico periodoAcademico);

    void eliminar(PeriodoAcademico periodoAcademico);

    PeriodoAcademico buscarPorId(Object id);

    List<PeriodoAcademico> buscarPorCriterio(PeriodoAcademico periodoAcademico);

    PeriodoAcademico buscarPorIdSga(PeriodoAcademico periodoAcademico);

}
