/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.Director;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface DirectorService {

    void guardar(final Director director);

    void actualizar(final Director director);

    void eliminar(final Director director);

    Director buscarPorId(final Director director);

    List<Director> buscar(final Director director);
}
