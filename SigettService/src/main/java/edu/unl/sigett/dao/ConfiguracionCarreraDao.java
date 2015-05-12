/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.ConfiguracionCarrera;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface ConfiguracionCarreraDao {

    void create(ConfiguracionCarrera configuracionCarrera);

    void edit(ConfiguracionCarrera configuracionCarrera);

    void remove(ConfiguracionCarrera configuracionCarrera);

    ConfiguracionCarrera find(Object id);

    List<ConfiguracionCarrera> findAll();

    List<ConfiguracionCarrera> findRange(int[] range);

    ConfiguracionCarrera buscarPorCarreraId(Integer carreraId, String codigo);

    List<ConfiguracionCarrera> buscar(ConfiguracionCarrera configuracionCarrera);

    int count();

}
