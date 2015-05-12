/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.ConfiguracionCarreraDao;
import edu.unl.sigett.entity.ConfiguracionCarrera;
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
public class ConfiguracionCarreraDaoImplement extends AbstractDao<ConfiguracionCarrera> implements ConfiguracionCarreraDao {

    public ConfiguracionCarreraDaoImplement() {
        super(ConfiguracionCarrera.class);
    }

    @Override
    public List<ConfiguracionCarrera> buscar(ConfiguracionCarrera configuracionCarrera) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("SELECT c FROM  ConfiguracionCarrera c WHERE 1=1 ");
        if (configuracionCarrera.getCarreraId() != 0) {
            sql.append(" and c.carreraId.id=:carreraId");
            parametros.put("carreraId", configuracionCarrera.getCarreraId());
        }
        if (configuracionCarrera.getCodigo() != null) {
            sql.append(" and c.codigo=:codigo");
            parametros.put("carreraId", configuracionCarrera.getCodigo());
        }
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }

    @Override
    public ConfiguracionCarrera buscarPorCarreraId(Integer carreraId, String codigo) {
        List<ConfiguracionCarrera> configuracionCarreras = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("SELECT c FROM  ConfiguracionCarrera c WHERE 1=1 ");
        if (carreraId != null) {
            sql.append(" and c.carreraId.id=:carreraId");
            parametros.put("carreraId", carreraId);
        }
        if (codigo != null) {
            sql.append(" and c.codigo=:codigo");
            parametros.put("carreraId", codigo);
        }
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        configuracionCarreras = q.getResultList();
        if (configuracionCarreras == null) {
            return null;
        }
        return !configuracionCarreras.isEmpty() ? configuracionCarreras.get(0) : null;
    }
}
