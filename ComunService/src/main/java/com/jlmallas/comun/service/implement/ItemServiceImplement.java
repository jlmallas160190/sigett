/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.service.implement;

import com.jlmallas.comun.dao.CatalogoDao;
import com.jlmallas.comun.dao.ItemDao;
import com.jlmallas.comun.entity.Catalogo;
import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.service.ItemService;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class ItemServiceImplement implements ItemService {
    
    @EJB
    private CatalogoDao catalogoDao;
    @EJB
    private ItemDao itemDao;
    
    @Override
    public List<Item> buscarPorCatalogo(final String codigoCatalogo) {
        List<Catalogo> catalogos = catalogoDao.buscar(new Catalogo(null, codigoCatalogo, codigoCatalogo, true, null));
        if (catalogos == null) {
            return new ArrayList<>();
        }
        Catalogo catalogo = !catalogos.isEmpty() ? catalogos.get(0) : null;
        if (catalogo == null) {
            return new ArrayList<>();
        }
        Item itemBuscar = new Item();
        itemBuscar.setCatalogoId(catalogo);
        return itemDao.buscar(itemBuscar);
    }
    
    @Override
    public Item buscarPorCatalogoCodigo(String codigoCatalogo, String codigo) {
        List<Catalogo> catalogos = catalogoDao.buscar(new Catalogo(null, null, codigoCatalogo, true, null));
        List<Item> items = new ArrayList<>();
        if (catalogos == null) {
            return null;
        }
        Catalogo catalogo = !catalogos.isEmpty() ? catalogos.get(0) : null;
        if (catalogo == null) {
            return null;
        }
        Item itemBuscar = new Item();
        itemBuscar.setCatalogoId(catalogo);
        itemBuscar.setCodigo(codigo);
        items = itemDao.buscar(itemBuscar);
        if (items == null) {
            return null;
        }
        return !items.isEmpty() ? items.get(0) : null;
    }
    
    @Override
    public Item buscarPorId(final Long itemId) {
        return itemDao.find(itemId);
    }
    
}
