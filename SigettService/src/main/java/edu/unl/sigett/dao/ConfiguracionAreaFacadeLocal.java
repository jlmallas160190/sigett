/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.ConfiguracionArea;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface ConfiguracionAreaFacadeLocal {

    void create(ConfiguracionArea configuracionArea);

    void edit(ConfiguracionArea configuracionArea);

    void remove(ConfiguracionArea configuracionArea);

    ConfiguracionArea find(Object id);

    List<ConfiguracionArea> findAll();

    List<ConfiguracionArea> findRange(int[] range);

    ConfiguracionArea buscarPorAreaId(Integer areaId,String codigo);

    int count();

}
