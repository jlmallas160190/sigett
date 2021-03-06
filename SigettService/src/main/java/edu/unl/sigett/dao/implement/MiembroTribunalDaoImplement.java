/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.MiembroTribunalDao;
import edu.unl.sigett.entity.MiembroTribunal;
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
public class MiembroTribunalDaoImplement extends AbstractDao<MiembroTribunal> implements MiembroTribunalDao {

    public MiembroTribunalDaoImplement() {
        super(MiembroTribunal.class);
    }

    @Override
    public List<MiembroTribunal> buscar(final MiembroTribunal miembroTribunal) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT m FROM MiembroTribunal m WHERE 1=1");
        if (miembroTribunal.getDocenteId() != null) {
            sql.append(" and m.docenteId=:docenteId ");
            parametros.put("docenteId", miembroTribunal.getDocenteId());
            existeFiltro = Boolean.TRUE;
        }
        if (miembroTribunal.getTribunalId() != null) {
            sql.append(" and m.tribunalId=:tribunalId ");
            parametros.put("tribunalId", miembroTribunal.getTribunalId());
            existeFiltro = Boolean.TRUE;
        }
        if (miembroTribunal.getEsActivo() != null) {
            sql.append(" and m.esActivo=:activo ");
            parametros.put("activo", miembroTribunal.getEsActivo());
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
