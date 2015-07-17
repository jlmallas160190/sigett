/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.dao.RangoEquivalenciaDao;
import edu.unl.sigett.entity.RangoEquivalencia;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class RangoEquivalenciaServiceImplement implements RangoEquivalenciaService {

    @EJB
    private RangoEquivalenciaDao rangoEquivalenciaDao;

    @Override
    public List<RangoEquivalencia> buscar(final RangoEquivalencia rangoEquivalencia) {
        List<RangoEquivalencia> rangoEquivalencias = rangoEquivalenciaDao.buscar(rangoEquivalencia);
        if (rangoEquivalencias == null) {
            return new ArrayList<>();
        }
        return rangoEquivalencias;
    }

}
