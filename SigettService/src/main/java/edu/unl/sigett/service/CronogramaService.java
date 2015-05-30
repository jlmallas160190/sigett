/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.Cronograma;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface CronogramaService {

    void guardar(final Cronograma cronograma);

    void actualizar(final Cronograma cronograma);

    void eliminar(final Cronograma cronograma);

    Cronograma buscarPorId(final Cronograma cronograma);

}
