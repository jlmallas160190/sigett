/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.DirectorDao;
import edu.unl.sigett.entity.Director;
import edu.unl.sigett.service.DirectorService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class DirectorServiceImplement implements DirectorService {

    @EJB
    private DirectorDao directorDao;

    @Override
    public void guardar(final Director director) {
        this.directorDao.create(director);
    }

    @Override
    public void actualizar(final Director director) {
        this.directorDao.edit(director);
    }

    @Override
    public void eliminar(final Director director) {
        this.directorDao.remove(director);
    }

    @Override
    public Director buscarPorId(final Director director) {
        return this.directorDao.find(director.getId());
    }

    @Override
    public List<Director> buscar(final Director director) {
        return this.directorDao.buscar(director);
    }
}
