/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.CalificacionParametro;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface CalificacionParametroFacadeLocal {

    void create(CalificacionParametro calificacionParametro);

    void edit(CalificacionParametro calificacionParametro);

    void remove(CalificacionParametro calificacionParametro);

    CalificacionParametro find(Object id);

    List<CalificacionParametro> findAll();

    List<CalificacionParametro> findRange(int[] range);

    List<CalificacionParametro> buscarPorCalificacionMiembro(Long calificacionMiembroId);

    CalificacionParametro buscarPorCalificacionMiembroParametro(Long calificacionMiembroId, Long parametroId);

    int count();

}
