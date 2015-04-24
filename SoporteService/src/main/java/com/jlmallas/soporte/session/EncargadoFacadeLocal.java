/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jlmallas.soporte.session;

import com.jlmallas.soporte.entity.Encargado;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface EncargadoFacadeLocal {

    void create(Encargado encargado);

    void edit(Encargado encargado);

    void remove(Encargado encargado);

    Encargado find(Object id);

    List<Encargado> findAll();

    List<Encargado> findRange(int[] range);

    int count();
    
}
