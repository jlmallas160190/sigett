/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.TipoProyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface TipoProyectoFacadeLocal {

    void create(TipoProyecto tipoProyecto);

    void edit(TipoProyecto tipoProyecto);

    void remove(TipoProyecto tipoProyecto);

    TipoProyecto find(Object id);

    List<TipoProyecto> findAll();

    List<TipoProyecto> findRange(int[] range);

    List<TipoProyecto> buscarActivos();

    List<TipoProyecto> buscarPorCriterio(String criterio);

    TipoProyecto buscarPorCodigo(String codigo);

    int count();

}
