/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.OficioCarrera;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface OficioCarreraService {

    void guardar(final OficioCarrera oficioCarrera);

    void actualizar(final OficioCarrera oficioCarrera);

    void remover(final OficioCarrera oficioCarrera);

    List<OficioCarrera> buscar(final OficioCarrera oficioCarrera);
}
