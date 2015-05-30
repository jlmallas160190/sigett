/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.TemaDao;
import edu.unl.sigett.entity.Tema;
import edu.unl.sigett.service.TemaService;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class TemaServiceImplement implements TemaService {

    @EJB
    private TemaDao temaDao;

    @Override
    public void guardar(final Tema tema) {
        this.temaDao.create(tema);
    }

    @Override
    public void actualizar(final Tema tema) {
        this.temaDao.edit(tema);
    }

    @Override
    public void eliminar(final Tema tema) {
        this.temaDao.remove(tema);
    }

    @Override
    public Tema buscarPorId(final Tema tema) {
        return this.temaDao.find(tema.getId());
    }
}
