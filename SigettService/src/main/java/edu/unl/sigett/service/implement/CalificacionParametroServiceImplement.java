/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.CalificacionParametroDao;
import edu.unl.sigett.entity.CalificacionParametro;
import edu.unl.sigett.service.CalificacionParametroService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class CalificacionParametroServiceImplement implements CalificacionParametroService {

    @EJB
    private CalificacionParametroDao calificacionParametroDao;

    @Override
    public void guardar(final CalificacionParametro calificacionParametro) {
        this.calificacionParametroDao.create(calificacionParametro);
    }

    @Override
    public void actualizar(final CalificacionParametro calificacionParametro) {
        this.calificacionParametroDao.edit(calificacionParametro);
    }

    @Override
    public void eliminar(final CalificacionParametro calificacionParametro) {
        this.calificacionParametroDao.remove(calificacionParametro);
    }

    @Override
    public CalificacionParametro buscarPorId(final CalificacionParametro calificacionParametro) {
        return this.calificacionParametroDao.find(calificacionParametro.getId());
    }

    @Override
    public List<CalificacionParametro> buscar(final CalificacionParametro calificacionParametro) {
        return this.calificacionParametroDao.buscar(calificacionParametro);
    }

}
