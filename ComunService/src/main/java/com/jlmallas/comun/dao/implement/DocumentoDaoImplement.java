/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.dao.implement;

import com.jlmallas.comun.dao.AbstractDao;
import com.jlmallas.comun.dao.DocumentoDao;
import com.jlmallas.comun.entity.Documento;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class DocumentoDaoImplement extends AbstractDao<Documento> implements DocumentoDao {

    public DocumentoDaoImplement() {
        super(Documento.class);
    }

    @Override
    public List<Documento> buscar(final Documento documento) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT d FROM Documento d  WHERE 1=1 ");
        if (documento.getCatalogoId() != null) {
            sql.append(" and d.catalogoId=:catalogoId");
            parametros.put("catalogoId", documento.getCatalogoId());
            existeFiltro = Boolean.TRUE;
        }
        if (!existeFiltro) {
            return null;
        }
        final Query q = em.createQuery(sql.toString());
        q.setMaxResults(1);
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }

}
