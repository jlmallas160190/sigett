/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.DocumentoProyectoDao;
import edu.unl.sigett.entity.DocumentoProyecto;
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
public class DocumentoProyectoDaoImplement extends AbstractDao<DocumentoProyecto> implements DocumentoProyectoDao {

    public DocumentoProyectoDaoImplement() {
        super(DocumentoProyecto.class);
    }

    @Override
    public List<DocumentoProyecto> buscar(final DocumentoProyecto documentoProyecto) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("Select d from DocumentoProyecto d WHERE 1=1 ");
        if (documentoProyecto.getProyectoId() != null) {
            sql.append(" and d.proyectoId=:proyectoId");
            parametros.put("proyectoId", documentoProyecto.getProyectoId());
            existeFiltro = Boolean.TRUE;
        }
        if (documentoProyecto.getDocumentoId() != null) {
            sql.append(" and d.documentoId=:documentoId");
            parametros.put("documentoId", documentoProyecto.getDocumentoId());
            existeFiltro = Boolean.TRUE;
        }
        if (documentoProyecto.getEsActivo() != null) {
            sql.append(" and d.esActivo=:activo");
            parametros.put("activo", documentoProyecto.getEsActivo());
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
