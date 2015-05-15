/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.dao.implement;

import com.jlmallas.comun.entity.Catalogo;
import com.jlmallas.comun.dao.AbstractDao;
import com.jlmallas.comun.dao.CatalogoDao;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class CatalogoDaoImplement extends AbstractDao<Catalogo> implements CatalogoDao {

    public CatalogoDaoImplement() {
        super(Catalogo.class);
    }

    @Override
    public List<Catalogo> buscar(Catalogo catalogo) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("SELECT c FROM Catalogo c WHERE 1=1 ");
        if (catalogo.getCodigo() != null) {
            sql.append(" and c.codigo=:codigo ");
            parametros.put("codigo", catalogo.getCodigo());
        }
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }

}
