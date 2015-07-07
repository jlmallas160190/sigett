/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.service;

import com.jlmallas.comun.entity.Item;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface ItemService {

    void guardar(final Item item);

    void actualizar(final Item item);

    List<Item> buscarPorCatalogo(String codigoCalogo);

    Item buscarPorCatalogoCodigo(String codigoCatalogo, String codigo);

    Item buscarPorId(final Long itemId);

}
