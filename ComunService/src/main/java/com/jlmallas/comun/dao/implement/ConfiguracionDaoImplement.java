/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.dao.implement;

import com.jlmallas.comun.dao.ConfiguracionDao;
import com.jlmallas.comun.entity.Configuracion;
import com.jlmallas.comun.dao.AbstractDao;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class ConfiguracionDaoImplement extends AbstractDao<Configuracion> implements ConfiguracionDao {


    public ConfiguracionDaoImplement() {
        super(Configuracion.class);
    }
   

    @Override
    public List<Configuracion> buscar(final Configuracion configuracion) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT c FROM Configuracion c  WHERE 1=1 ");
        if (configuracion.getCodigo() != null) {
            sql.append(" and c.codigo=:codigo");
            parametros.put("codigo", configuracion.getCodigo());
            existeFiltro = Boolean.TRUE;
        }
        if (!existeFiltro) {
            return null;
        }
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }

}
