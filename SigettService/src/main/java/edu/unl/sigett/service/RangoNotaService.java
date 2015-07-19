/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.RangoNota;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface RangoNotaService {

    RangoNota buscarPorId(final RangoNota rangoNota);

    List<RangoNota> buscar(final RangoNota rangoNota);

    RangoNota buscarPorCodigo(final RangoNota rangoNota);
}
