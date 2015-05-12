/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.dao;

import com.jlmallas.comun.entity.Configuracion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface ConfiguracionDao {

    void create(Configuracion configuracionGeneral);

    void edit(Configuracion configuracionGeneral);

    void remove(Configuracion configuracionGeneral);

    Configuracion find(Object id);

    List<Configuracion> findAll();

    List<Configuracion> findRange(int[] range);

    String getSecretKey();

    Configuracion buscarPorCodigo(String codigo);

    String desencriptaClave(String clave);

    String encriptaClave(String clave);

    int count();

}
