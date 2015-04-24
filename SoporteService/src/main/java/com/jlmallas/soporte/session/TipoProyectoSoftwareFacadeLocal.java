/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jlmallas.soporte.session;

import com.jlmallas.soporte.entity.TipoProyectoSoftware;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface TipoProyectoSoftwareFacadeLocal {

    void create(TipoProyectoSoftware tipoProyectoSoftware);

    void edit(TipoProyectoSoftware tipoProyectoSoftware);

    void remove(TipoProyectoSoftware tipoProyectoSoftware);

    TipoProyectoSoftware find(Object id);

    List<TipoProyectoSoftware> findAll();

    List<TipoProyectoSoftware> findRange(int[] range);

    int count();
    
}
