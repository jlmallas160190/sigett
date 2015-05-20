/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.LineaInvestigacionCarreraDao;
import edu.unl.sigett.entity.LineaInvestigacionCarrera;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class LineaInvestigacionCarreraDaoImplement extends AbstractDao<LineaInvestigacionCarrera> implements LineaInvestigacionCarreraDao {

    public LineaInvestigacionCarreraDaoImplement() {
        super(LineaInvestigacionCarrera.class);
    }


    @Override
    public List<LineaInvestigacionCarrera> buscar(LineaInvestigacionCarrera lic) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("SELECT lc FROM  LineaInvestigacionCarrera lc where 1=1 ");
        if (lic.getLineaInvestigacionId() != null) {
            sql.append(" and lc.lineaInvestigacionId=:lineaInvestigacion");
            parametros.put("lineaInvestigacion", lic.getLineaInvestigacionId());
        }
        if (lic.getCarreraId() != null) {
            sql.append(" and lc.carreraId=:carreraId");
            parametros.put("carreraId", lic.getCarreraId());
        }
        sql.append(" and lc.lineaInvestigacionId.esActivo=TRUE ");
        sql.append(" order by lc.lineaInvestigacionId.nombre ");
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();

    }
}
