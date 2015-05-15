/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao.implement;

import edu.jlmallas.academico.entity.PeriodoCoordinacion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface PeriodoCoordinacionFacadeLocal {

    void create(PeriodoCoordinacion periodoCoordinacion);

    void edit(PeriodoCoordinacion periodoCoordinacion);

    void remove(PeriodoCoordinacion periodoCoordinacion);

    PeriodoCoordinacion find(Object id);

    List<PeriodoCoordinacion> findAll();

    List<PeriodoCoordinacion> findRange(int[] range);

    List<PeriodoCoordinacion> buscarPorCarrera(Integer carreraId);

    List<PeriodoCoordinacion> buscarActivos();

    int count();

}
