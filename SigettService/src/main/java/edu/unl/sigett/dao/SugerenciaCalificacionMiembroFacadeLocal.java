/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.SugerenciaCalificacionMiembro;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface SugerenciaCalificacionMiembroFacadeLocal {

    void create(SugerenciaCalificacionMiembro sugerenciaCalificacionMiembro);

    void edit(SugerenciaCalificacionMiembro sugerenciaCalificacionMiembro);

    void remove(SugerenciaCalificacionMiembro sugerenciaCalificacionMiembro);

    SugerenciaCalificacionMiembro find(Object id);

    List<SugerenciaCalificacionMiembro> findAll();

    List<SugerenciaCalificacionMiembro> findRange(int[] range);

    List<SugerenciaCalificacionMiembro> buscarPorCalificacioMiembro(Long calificacionMiembroId);

    int count();

}
