/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jlmallas.soporte.session;

import com.jlmallas.soporte.entity.EncargadoProyectoSoftware;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface EncargadoProyectoSoftwareFacadeLocal {

    void create(EncargadoProyectoSoftware encargadoProyectoSoftware);

    void edit(EncargadoProyectoSoftware encargadoProyectoSoftware);

    void remove(EncargadoProyectoSoftware encargadoProyectoSoftware);

    EncargadoProyectoSoftware find(Object id);

    List<EncargadoProyectoSoftware> findAll();

    List<EncargadoProyectoSoftware> findRange(int[] range);

    int count();
    
}
