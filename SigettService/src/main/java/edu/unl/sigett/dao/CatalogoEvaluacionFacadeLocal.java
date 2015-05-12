/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.CatalogoEvaluacion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface CatalogoEvaluacionFacadeLocal {

    void create(CatalogoEvaluacion catalogoEvaluacion);

    void edit(CatalogoEvaluacion catalogoEvaluacion);

    void remove(CatalogoEvaluacion catalogoEvaluacion);

    CatalogoEvaluacion find(Object id);

    List<CatalogoEvaluacion> findAll();

    List<CatalogoEvaluacion> findRange(int[] range);

    List<CatalogoEvaluacion> buscarActivos();

    CatalogoEvaluacion buscarPorCodigo(String codigo);

    int count();

}
