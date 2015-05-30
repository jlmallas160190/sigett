/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.Tema;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface TemaService {

    void guardar(final Tema tema);

    void actualizar(final Tema tema);

    void eliminar(final Tema tema);

    Tema buscarPorId(final Tema tema);
}
