/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.soporte.session;

import com.jlmallas.soporte.entity.Objeto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface ObjetoFacadeLocal {

    void create(Objeto objeto);

    void edit(Objeto objeto);

    void remove(Objeto objeto);

    Objeto find(Object id);

    List<Objeto> findAll();

    List<Objeto> findRange(int[] range);

    Objeto buscarPorNombre(String nombre);

    List<Objeto> buscarPorOrdenAlfabetico();

    int count();

}
