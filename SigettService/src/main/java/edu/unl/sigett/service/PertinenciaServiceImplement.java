/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.dao.PertinenciaDao;
import edu.unl.sigett.entity.Pertinencia;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class PertinenciaServiceImplement implements PertinenciaService {

    @EJB
    private PertinenciaDao pertinenciaDao;

    @Override
    public void guardar(final Pertinencia pertinencia) {
        this.pertinenciaDao.create(pertinencia);
    }

    @Override
    public void actualizar(final Pertinencia pertinencia) {
        this.pertinenciaDao.edit(pertinencia);
    }

    @Override
    public void eliminar(final Pertinencia pertinencia) {
        this.pertinenciaDao.remove(pertinencia);
    }

    @Override
    public Pertinencia buscarPorId(final Pertinencia pertinencia) {
        return this.pertinenciaDao.find(pertinencia.getId());
    }

    @Override
    public List<Pertinencia> buscar(final Pertinencia pertinencia) {
        return this.pertinenciaDao.buscar(pertinencia);
    }

}
