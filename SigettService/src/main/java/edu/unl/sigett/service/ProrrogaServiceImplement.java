/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.dao.ProrrogaDao;
import edu.unl.sigett.entity.Prorroga;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class ProrrogaServiceImplement implements ProrrogaService {

    @EJB
    private ProrrogaDao prorrogaDao;

    @Override
    public void guardar(final Prorroga prorroga) {
        this.prorrogaDao.create(prorroga);
    }

    @Override
    public void actualizar(final Prorroga prorroga) {
        this.prorrogaDao.edit(prorroga);
    }

    @Override
    public void eliminar(final Prorroga prorroga) {
        this.prorrogaDao.remove(prorroga);
    }

    @Override
    public Prorroga buscarPorId(final Prorroga prorroga) {
        return prorrogaDao.find(prorroga.getId());
    }

    @Override
    public List<Prorroga> buscar(final Prorroga prorroga) {
        List<Prorroga> prorrogas = prorrogaDao.buscar(prorroga);
        if (prorrogas == null) {
            return new ArrayList<>();
        }
        return prorrogas;
    }

    @Override
    public Prorroga obtenerPorFechaMayor(final Prorroga prorroga) {
        return prorrogaDao.obtenerPorFechaMayor(prorroga);
    }

}
