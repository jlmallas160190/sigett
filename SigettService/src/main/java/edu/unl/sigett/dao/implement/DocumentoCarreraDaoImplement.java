/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.DocumentoCarreraDao;
import edu.unl.sigett.entity.DocumentoCarrera;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class DocumentoCarreraDaoImplement extends AbstractDao<DocumentoCarrera> implements DocumentoCarreraDao {

    public DocumentoCarreraDaoImplement() {
        super(DocumentoCarrera.class);
    }

    @Override
    public List<DocumentoCarrera> buscar(DocumentoCarrera documentoCarrera) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT d FROM DocumentoCarrera d  WHERE 1=1 ");
        if (documentoCarrera.getCarreraId() != null) {
            sql.append(" and d.carreraId=:carreraId");
            parametros.put("carreraId", documentoCarrera.getCarreraId());
            existeFiltro = Boolean.TRUE;
        }
        if (documentoCarrera.getEsActivo() != null) {
            sql.append(" and d.esActivo=:activo");
            parametros.put("activo", documentoCarrera.getEsActivo());
        }
        if (documentoCarrera.getDocumentoId() != null) {
            sql.append(" and d.documentoId=:documento");
            parametros.put("documento", documentoCarrera.getDocumentoId());
            existeFiltro = Boolean.TRUE;
        }
        if (!existeFiltro) {
            return new ArrayList<>();
        }
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }

}
