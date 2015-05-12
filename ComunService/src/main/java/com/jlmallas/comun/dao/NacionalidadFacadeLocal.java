/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.dao;

import com.jlmallas.comun.entity.Nacionalidad;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface NacionalidadFacadeLocal {

    void create(Nacionalidad nacionalidad);

    void edit(Nacionalidad nacionalidad);

    void remove(Nacionalidad nacionalidad);

    Nacionalidad find(Object id);

    List<Nacionalidad> findAll();

    List<Nacionalidad> findRange(int[] range);

    int count();

}
