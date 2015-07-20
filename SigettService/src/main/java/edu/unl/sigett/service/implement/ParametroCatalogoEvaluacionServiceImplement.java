/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.ParametroCatalogoEvaluacionDao;
import edu.unl.sigett.entity.ParametroCatalogoEvaluacion;
import edu.unl.sigett.service.ParametroCatalogoEvaluacionService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class ParametroCatalogoEvaluacionServiceImplement implements ParametroCatalogoEvaluacionService {

    @EJB
    private ParametroCatalogoEvaluacionDao parametroCatalogoEvaluacionDao;

    @Override
    public void guardar(final ParametroCatalogoEvaluacion parametroCatalogoEvaluacion) {
        this.parametroCatalogoEvaluacionDao.create(parametroCatalogoEvaluacion);
    }

    @Override
    public void actualizar(final ParametroCatalogoEvaluacion parametroCatalogoEvaluacion) {
        this.parametroCatalogoEvaluacionDao.edit(parametroCatalogoEvaluacion);
    }

    @Override
    public void eliminar(final ParametroCatalogoEvaluacion parametroCatalogoEvaluacion) {
        this.parametroCatalogoEvaluacionDao.remove(parametroCatalogoEvaluacion);
    }

    @Override
    public ParametroCatalogoEvaluacion buscarPorId(final ParametroCatalogoEvaluacion parametroCatalogoEvaluacion) {
        return this.parametroCatalogoEvaluacionDao.find(parametroCatalogoEvaluacion.getId());
    }

    @Override
    public List<ParametroCatalogoEvaluacion> buscar(final ParametroCatalogoEvaluacion parametroCatalogoEvaluacion) {
        return this.parametroCatalogoEvaluacionDao.buscar(parametroCatalogoEvaluacion);
    }
}
