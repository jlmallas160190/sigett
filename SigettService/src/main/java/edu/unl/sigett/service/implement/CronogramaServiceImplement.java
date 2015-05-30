/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.CronogramaDao;
import edu.unl.sigett.entity.Cronograma;
import edu.unl.sigett.service.CronogramaService;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class CronogramaServiceImplement implements CronogramaService {

    @EJB
    private CronogramaDao cronogramaDao;

    @Override
    public void guardar(final Cronograma cronograma) {
        this.cronogramaDao.create(cronograma);
    }

    @Override
    public void actualizar(final Cronograma cronograma) {
        this.cronogramaDao.edit(cronograma);
    }

    @Override
    public void eliminar(final Cronograma cronograma) {
        this.cronogramaDao.remove(cronograma);
    }

    @Override
    public Cronograma buscarPorId(final Cronograma cronograma) {
        return this.cronogramaDao.find(cronograma.getId());
    }

}
