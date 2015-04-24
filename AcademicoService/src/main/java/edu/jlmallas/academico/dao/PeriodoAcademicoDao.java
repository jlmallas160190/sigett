/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao;

import edu.jlmallas.academico.entity.PeriodoAcademico;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface PeriodoAcademicoDao {

    void create(PeriodoAcademico periodoAcademico);

    void edit(PeriodoAcademico periodoAcademico);

    void remove(PeriodoAcademico periodoAcademico);

    PeriodoAcademico find(Object id);

    List<PeriodoAcademico> findAll();

    List<PeriodoAcademico> findRange(int[] range);

    List<PeriodoAcademico> buscarPorCriterio(PeriodoAcademico periodoAcademico);

    PeriodoAcademico buscarPorIdSga(PeriodoAcademico periodoAcademico);
}
