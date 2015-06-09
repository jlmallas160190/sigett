/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.RenunciaDirectorDao;
import edu.unl.sigett.entity.RenunciaDirector;
import edu.unl.sigett.service.RenunciaDirectorService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class RenunciaDirectorServiceImplement implements RenunciaDirectorService {

    @EJB
    private RenunciaDirectorDao renunciaDirectorDao;

    @Override
    public void guardar(final RenunciaDirector renunciaDirector) {
        this.renunciaDirectorDao.create(renunciaDirector);
    }

    @Override
    public void actualizar(final RenunciaDirector renunciaDirector) {
        this.renunciaDirectorDao.edit(renunciaDirector);
    }

    @Override
    public void eliminar(final RenunciaDirector renunciaDirector) {
        this.renunciaDirectorDao.remove(renunciaDirector);
    }

    @Override
    public RenunciaDirector buscarPorId(final RenunciaDirector renunciaDirector) {
        return this.renunciaDirectorDao.find(renunciaDirector.getId());
    }

    @Override
    public List<RenunciaDirector> buscar(final RenunciaDirector renunciaDirector) {
        return this.renunciaDirectorDao.buscar(renunciaDirector);
    }

}
