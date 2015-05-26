/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.OficioCarreraDao;
import edu.unl.sigett.entity.OficioCarrera;
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
public class OficioCarreraDaoImplement extends AbstractDao<OficioCarrera> implements OficioCarreraDao {

    public OficioCarreraDaoImplement() {
        super(OficioCarrera.class);
    }

    @Override
    public List<OficioCarrera> buscarPorCarrera(Integer carreraId) {
        try {
            Query query = em.createQuery("SELECT o FROM OficioCarrera o WHERE" + " (o.carreraId.id=:id)");
            query.setParameter("id", carreraId);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public OficioCarrera buscarPorTablaId(Long id, String catalogoCodigo) {
        List<OficioCarrera> oficioCarreras = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT o FROM OficioCarrera o WHERE" + " (o.tablaOficioId=:id and o.catalogoOficioId.codigo=:catalogoCodigo)");
            query.setParameter("id", id);
            query.setParameter("catalogoCodigo", catalogoCodigo);
            oficioCarreras = query.getResultList();
            return !oficioCarreras.isEmpty() ? oficioCarreras.get(0) : null;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public List<OficioCarrera> buscar(OficioCarrera oficioCarrera) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT o FROM OficioCarrera o  WHERE 1=1 ");
        if (oficioCarrera.getCarreraId() != null) {
            sql.append(" and o.carreraId=:carreraId");
            parametros.put("carreraId", oficioCarrera.getCarreraId());
            existeFiltro = Boolean.TRUE;
        }
        if (oficioCarrera.getEsActivo() != null) {
            sql.append(" and o.esActivo=:activo");
            parametros.put("activo", oficioCarrera.getEsActivo());
            existeFiltro = Boolean.TRUE;
        }
        if (oficioCarrera.getCatalogoOficioId() != null) {
            sql.append(" and o.catalogoOficioId=:catalogo");
            parametros.put("catalogo", oficioCarrera.getCatalogoOficioId());
            existeFiltro = Boolean.TRUE;
        }
        if (oficioCarrera.getTablaOficioId() != null) {
            sql.append(" and o.tablaOficioId=:tabla");
            parametros.put("tabla", oficioCarrera.getCatalogoOficioId());
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
