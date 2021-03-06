/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.ConfiguracionProyectoDao;
import edu.unl.sigett.entity.ConfiguracionProyecto;
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
public class ConfiguracionProyectoDaoImplement extends AbstractDao<ConfiguracionProyecto> implements ConfiguracionProyectoDao {

    public ConfiguracionProyectoDaoImplement() {
        super(ConfiguracionProyecto.class);
    }

    @Override
    public List<ConfiguracionProyecto> buscar(final ConfiguracionProyecto configuracionProyecto) {
        Boolean existeFiltro = Boolean.FALSE;
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("SELECT c FROM ConfiguracionProyecto c where 1=1 ");
        if (configuracionProyecto.getProyectoId() != null) {
            sql.append(" and c.proyectoId=:proyectoId");
            parametros.put("proyectoId", configuracionProyecto.getProyectoId());
            existeFiltro = Boolean.TRUE;
        }
        if (configuracionProyecto.getCodigo() != null) {
            sql.append(" and c.codigo=:codigo");
            parametros.put("codigo", configuracionProyecto.getCodigo());
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
