/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.ConfiguracionCarrera;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface ConfiguracionCarreraService {

    void guardar(ConfiguracionCarrera configuracionCarrera);

    void actualizar(final ConfiguracionCarrera configuracionCarrera);

    List<ConfiguracionCarrera> buscar(final ConfiguracionCarrera configuracionCarrera);

    ConfiguracionCarrera buscarPrimero(final ConfiguracionCarrera configuracionCarrera);

    ConfiguracionCarrera buscarPorId(final ConfiguracionCarrera configuracionCarrera);
}
