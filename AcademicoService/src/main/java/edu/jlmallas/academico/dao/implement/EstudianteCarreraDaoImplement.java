/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao.implement;

import edu.jlmallas.academico.dao.EstudianteCarreraDao;
import edu.jlmallas.academico.dao.AbstractDao;
import edu.jlmallas.academico.entity.EstudianteCarrera;
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
public class EstudianteCarreraDaoImplement extends AbstractDao<EstudianteCarrera> implements EstudianteCarreraDao {

    public EstudianteCarreraDaoImplement() {
        super(EstudianteCarrera.class);
    }

    @Override
    public List<EstudianteCarrera> buscar(EstudianteCarrera estudianteCarrera) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT ec FROM EstudianteCarrera ec WHERE 1=1 ");
        if (estudianteCarrera.getCarreraId() != null) {
            sql.append(" and ec.carreraId=:carreraId ");
            parametros.put("carreraId", estudianteCarrera.getCarreraId());
            existeFiltro = Boolean.TRUE;
        }
        if (estudianteCarrera.getEstudianteId() != null) {
            sql.append(" and ec.estudianteId=:estudianteId");
            parametros.put("estudianteId", estudianteCarrera.getEstudianteId());
            existeFiltro = Boolean.TRUE;
        }
        if (estudianteCarrera.getOfertaAcademica() != null) {
            sql.append(" and ec.ofertaAcademica=:ofertaAcademica");
            parametros.put("ofertaAcademica", estudianteCarrera.getOfertaAcademica());
            existeFiltro = Boolean.TRUE;
        }
        if (estudianteCarrera.getEsActivo() != null) {
            sql.append(" and ec.esActivo=:activo");
            parametros.put("activo", estudianteCarrera.getEsActivo());
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
