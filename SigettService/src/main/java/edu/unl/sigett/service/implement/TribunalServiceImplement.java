/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.TribunalDao;
import edu.unl.sigett.entity.Tribunal;
import edu.unl.sigett.service.TribunalService;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class TribunalServiceImplement implements TribunalService {

    @EJB
    private TribunalDao tribunalDao;

    @Override
    public void guardar(final Tribunal tribunal) {
        this.tribunalDao.create(tribunal);
    }

    @Override
    public void actualizar(final Tribunal tribunal) {
        this.tribunalDao.edit(tribunal);
    }

    @Override
    public void eliminar(final Tribunal tribunal) {
        this.tribunalDao.remove(tribunal);
    }

    @Override
    public Tribunal buscarPorId(final Tribunal tribunal) {
        return this.tribunalDao.find(tribunal.getId());
    }

    @Override
    public List<Tribunal> buscar(Tribunal tribunal) {
        List<Tribunal> tribunales = this.tribunalDao.buscar(tribunal);
        if (tribunales == null) {
            return new ArrayList<>();
        }
        return tribunales;
    }

}
