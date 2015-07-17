/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.MiembroTribunalDao;
import edu.unl.sigett.entity.MiembroTribunal;
import edu.unl.sigett.service.MiembroTribunalService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class MiembroTribunalServiceImplement implements MiembroTribunalService {

    @EJB
    private MiembroTribunalDao miembroTribunalDao;

    @Override
    public void guardar(final MiembroTribunal miembroTribunal) {
        this.miembroTribunalDao.create(miembroTribunal);
    }

    @Override
    public void actualizar(final MiembroTribunal miembroTribunal) {
        this.miembroTribunalDao.edit(miembroTribunal);
    }

    @Override
    public void eliminar(final MiembroTribunal miembroTribunal) {
        this.miembroTribunalDao.remove(miembroTribunal);
    }

    @Override
    public MiembroTribunal buscarPorId(MiembroTribunal miembroTribunal) {
        return this.miembroTribunalDao.find(miembroTribunal.getId());
    }

    @Override
    public List<MiembroTribunal> buscar(final MiembroTribunal miembroTribunal) {
        return this.miembroTribunalDao.buscar(miembroTribunal);
    }

}
