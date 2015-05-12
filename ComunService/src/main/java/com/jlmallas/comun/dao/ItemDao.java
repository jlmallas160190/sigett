/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.dao;

import com.jlmallas.comun.entity.Item;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface ItemDao {

    void create(Item item);

    void edit(Item item);

    void remove(Item item);

    Item find(Object id);

    List<Item> findAll();

    List<Item> findRange(int[] range);

    Item buscarPorCodigo(String codigoCatalogo, String codigo);

    List<Item> buscarPorCatalogo(String codigoCatalogo);

    int count();

}
