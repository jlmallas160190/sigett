/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jlmallas.comun.dao;

import com.jlmallas.comun.entity.Duracion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface DuracionDao {

    void create(Duracion duracion);

    void edit(Duracion duracion);

    void remove(Duracion duracion);

    Duracion find(Object id);

    List<Duracion> findAll();

    List<Duracion> findRange(int[] range);

    int count();
    
}
