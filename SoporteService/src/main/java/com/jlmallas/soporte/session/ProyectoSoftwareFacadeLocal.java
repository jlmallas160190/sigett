/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jlmallas.soporte.session;

import com.jlmallas.soporte.entity.ProyectoSoftware;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface ProyectoSoftwareFacadeLocal {

    void create(ProyectoSoftware proyectoSoftware);

    void edit(ProyectoSoftware proyectoSoftware);

    void remove(ProyectoSoftware proyectoSoftware);

    ProyectoSoftware find(Object id);

    List<ProyectoSoftware> findAll();

    List<ProyectoSoftware> findRange(int[] range);

    int count();
    
}
