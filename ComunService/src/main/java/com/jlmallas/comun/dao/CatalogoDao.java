/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.dao;

import com.jlmallas.comun.entity.Catalogo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface CatalogoDao {

    void create(Catalogo catalogo);

    void edit(Catalogo catalogo);

    void remove(Catalogo catalogo);

    Catalogo find(Object id);

    List<Catalogo> findAll();

    List<Catalogo> findRange(int[] range);

    List<Catalogo> buscar(Catalogo catalogo);

    int count();

}
