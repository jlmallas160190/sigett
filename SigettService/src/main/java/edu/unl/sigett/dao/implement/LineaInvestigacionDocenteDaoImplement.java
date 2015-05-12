/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.LineaInvestigacionDocenteDao;
import edu.unl.sigett.entity.LineaInvestigacionDocente;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class LineaInvestigacionDocenteDaoImplement extends AbstractDao<LineaInvestigacionDocente> implements LineaInvestigacionDocenteDao {

    public LineaInvestigacionDocenteDaoImplement() {
        super(LineaInvestigacionDocente.class);
    }

    @Override
    public List<LineaInvestigacionDocente> buscarPorDocenteCi(String numeroIdentificacion) {
        try {
            Query query = em.createQuery("SELECT ld FROM LineaInvestigacionDocente ld WHERE " + "(ld.docenteId.persona.numeroIdentificacion=:numeroIdentificacion)");
            query.setParameter("numeroIdentificacion", numeroIdentificacion);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public List<LineaInvestigacionDocente> buscar(LineaInvestigacionDocente lid) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("SELECT ld FROM LineaInvestigacionDocente ld where 1=1 ");
        if (lid.getDocenteId() != null) {
            sql.append(" and ld.docenteId=:docenteId ");
            parametros.put("docenteId", lid.getDocenteId());
        }
        sql.append(" order by ld.lineaInvestigacionId.nombre ");
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }
}
