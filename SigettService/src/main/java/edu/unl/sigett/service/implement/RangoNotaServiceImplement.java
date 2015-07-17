/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.RangoNotaDao;
import edu.unl.sigett.entity.RangoNota;
import edu.unl.sigett.service.RangoNotaService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class RangoNotaServiceImplement implements RangoNotaService {

    @EJB
    private RangoNotaDao rangoNotaDao;

    @Override
    public RangoNota buscarPorId(final RangoNota rangoNota) {
        return this.rangoNotaDao.find(rangoNota.getId());
    }

    @Override
    public List<RangoNota> buscar(final RangoNota rangoNota) {
        return this.rangoNotaDao.buscar(rangoNota);
    }

}
