/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.ParametroCatalogoEvaluacion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface ParametroCatalogoEvaluacionService {

    void guardar(final ParametroCatalogoEvaluacion parametroCatalogoEvaluacion);

    void actualizar(final ParametroCatalogoEvaluacion parametroCatalogoEvaluacion);

    void eliminar(final ParametroCatalogoEvaluacion parametroCatalogoEvaluacion);

    ParametroCatalogoEvaluacion buscarPorId(final ParametroCatalogoEvaluacion parametroCatalogoEvaluacion);

    List<ParametroCatalogoEvaluacion> buscar(final ParametroCatalogoEvaluacion parametroCatalogoEvaluacion);
}
