/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.ParametroCatalogoEvaluacion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface ParametroCatalogoEvaluacionFacadeLocal {

    void create(ParametroCatalogoEvaluacion parametroCatalogoEvaluacion);

    void edit(ParametroCatalogoEvaluacion parametroCatalogoEvaluacion);

    void remove(ParametroCatalogoEvaluacion parametroCatalogoEvaluacion);

    ParametroCatalogoEvaluacion find(Object id);

    List<ParametroCatalogoEvaluacion> findAll();

    List<ParametroCatalogoEvaluacion> findRange(int[] range);

    List<ParametroCatalogoEvaluacion> buscarPorCatalogoEvaluacion(Integer catalogoEvaluacionId);

    int count();

}
