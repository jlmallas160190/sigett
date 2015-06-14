/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.DocumentoActividadDao;
import edu.unl.sigett.entity.DocumentoActividad;
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
public class DocumentoActividadDaoImplement extends AbstractDao<DocumentoActividad> implements DocumentoActividadDao {

    public DocumentoActividadDaoImplement() {
        super(DocumentoActividad.class);
    }

    @Override
    public List<DocumentoActividad> buscar(DocumentoActividad documentoActividad) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT d FROM DocumentoActividad d  WHERE 1=1 ");
        if (documentoActividad.getActividadId() != null) {
            sql.append(" and d.actividadId=:actividadId");
            parametros.put("actividadId", documentoActividad.getActividadId());
            existeFiltro = Boolean.TRUE;
        }
        if (documentoActividad.getDocumentoId() != null) {
            sql.append(" and d.documentoId=:documentoId");
            parametros.put("documentoId", documentoActividad.getDocumentoId());
            existeFiltro = Boolean.TRUE;
        }
        if (documentoActividad.getEsActual() != null) {
            sql.append(" and d.esActual=:actual");
            parametros.put("actual", documentoActividad.getEsActual());
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
