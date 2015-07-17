/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.RangoEquivalencia;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface RangoEquivalenciaService {

    List<RangoEquivalencia> buscar(final RangoEquivalencia rangoEquivalencia);
}
