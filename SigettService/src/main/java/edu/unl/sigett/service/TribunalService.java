/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.Tribunal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface TribunalService {

    void guardar(final Tribunal tribunal);

    void actualizar(final Tribunal tribunal);

    void eliminar(final Tribunal tribunal);

    Tribunal buscarPorId(final Tribunal tribunal);

    List<Tribunal> buscar(final Tribunal tribunal);
}
