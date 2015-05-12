/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.dao.implement;

import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.dao.AbstractDao;
import com.jlmallas.comun.dao.ItemDao;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class ItemDaoImplement extends AbstractDao<Item> implements ItemDao {

    public ItemDaoImplement() {
        super(Item.class);
    }

    @Override
    public Item buscarPorCodigo(String codigoCatalogo, String codigo) {
        List<Item> items = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT i FROM Item i WHERE" + " (i.codigo=:codigo and i.catalogoId.codigo=:codigoCatlogo and  i.esActivo=TRUE)");
            query.setParameter("codigo", codigo);
            query.setParameter("codigoCatalogo", codigoCatalogo);
            items = query.getResultList();
            return !items.isEmpty() ? items.get(0) : null;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public List<Item> buscarPorCatalogo(String codigoCatalogo) {
        try {
            Query query = em.createQuery("SELECT i FROM Item i WHERE" + " (i.catalogoId.codigo=:codigoCatlogo and i.esActivo=TRUE)");
            query.setParameter("codigoCatalogo", codigoCatalogo);
            return query.getResultList();

        } catch (Exception e) {
        }
        return null;
    }
}
