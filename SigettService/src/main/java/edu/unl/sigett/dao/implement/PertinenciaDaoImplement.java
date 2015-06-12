/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.PertinenciaDao;
import edu.unl.sigett.entity.Pertinencia;
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
public class PertinenciaDaoImplement extends AbstractDao<Pertinencia> implements PertinenciaDao {

    public PertinenciaDaoImplement() {
        super(Pertinencia.class);
    }

    @Override
    public List<Pertinencia> buscar(Pertinencia pertinencia) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("Select p from Pertinencia p where 1=1");
        if (pertinencia.getEsAceptado() != null) {
            sql.append(" and p.esAceptado=:aceptado");
            parametros.put("aceptado", pertinencia.getEsAceptado());
            existeFiltro = Boolean.TRUE;
        }
        if (pertinencia.getEsActivo() != null) {
            sql.append(" and p.esActivo=:activo");
            parametros.put("activo", pertinencia.getEsActivo());
            existeFiltro = Boolean.TRUE;
        }
        if (pertinencia.getDocenteProyectoId() != null) {
            sql.append(" and p.docenteProyectoId=:docenteProyecto");
            parametros.put("docenteProyecto", pertinencia.getDocenteProyectoId());
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
