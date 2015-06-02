/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.LineaInvestigacionProyectoDao;
import edu.unl.sigett.entity.LineaInvestigacionProyecto;
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
public class LineaInvestigacionProyectoDaoImplement extends AbstractDao<LineaInvestigacionProyecto> implements LineaInvestigacionProyectoDao {

    public LineaInvestigacionProyectoDaoImplement() {
        super(LineaInvestigacionProyecto.class);
    }

    @Override
    public List<LineaInvestigacionProyecto> buscar(LineaInvestigacionProyecto lineaInvestigacionProyecto) {
        Boolean existeFiltro = Boolean.FALSE;
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("Select lp from LineaInvestigacionProyecto lp where 1=1");
        if (lineaInvestigacionProyecto.getLineaInvestigacionId() != null) {
            sql.append(" and lp.lineaInvestigacionId=:lineaInvestigacion");
            parametros.put("lineaInvestigacion", lineaInvestigacionProyecto.getLineaInvestigacionId());
            existeFiltro = Boolean.TRUE;
        }
        if (lineaInvestigacionProyecto.getProyectoId() != null) {
            sql.append(" and lp.proyectoId=:proyectoId");
            parametros.put("proyectoId", lineaInvestigacionProyecto.getProyectoId());
            existeFiltro = Boolean.TRUE;
        }
        if (!existeFiltro) {
            return new ArrayList<>();
        }
        sql.append(" order by lp.lineaInvestigacionId.nombre ");
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }
}
