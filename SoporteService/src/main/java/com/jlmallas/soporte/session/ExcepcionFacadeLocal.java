/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.soporte.session;

import com.jlmallas.soporte.entity.Excepcion;
import com.jlmallas.soporte.entity.Objeto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface ExcepcionFacadeLocal {

    void create(Excepcion excepcion);

    void edit(Excepcion excepcion);

    void remove(Excepcion excepcion);

    Excepcion find(Object id);

    List<Excepcion> findAll();

    List<Excepcion> findRange(int[] range);

    Excepcion crearExcepcion(String usuario, String descripcion, Objeto obj);

    int count();

}
