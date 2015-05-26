/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.OficioCarreraDao;
import edu.unl.sigett.entity.OficioCarrera;
import edu.unl.sigett.service.OficioCarreraService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class OficioCarreraServiceImplement implements OficioCarreraService {

    @EJB
    private OficioCarreraDao oficioCarreraDao;

    @Override
    public void guardar(final OficioCarrera oficioCarrera) {
        this.oficioCarreraDao.create(oficioCarrera);
    }

    @Override
    public void actualizar(final OficioCarrera oficioCarrera) {
        this.oficioCarreraDao.edit(oficioCarrera);
    }

    @Override
    public void remover(final OficioCarrera oficioCarrera) {
        this.oficioCarreraDao.remove(oficioCarrera);
    }

    @Override
    public List<OficioCarrera> buscar(final OficioCarrera oficioCarrera) {
        return this.oficioCarreraDao.buscar(oficioCarrera);
    }

}
