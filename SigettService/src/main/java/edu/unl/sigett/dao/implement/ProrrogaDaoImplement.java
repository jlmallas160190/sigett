/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.ProrrogaDao;
import edu.unl.sigett.entity.Prorroga;
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
public class ProrrogaDaoImplement extends AbstractDao<Prorroga> implements ProrrogaDao {

    public ProrrogaDaoImplement() {
        super(Prorroga.class);
    }

    @Override
    public List<Prorroga> buscar(final Prorroga prorroga) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT p FROM Prorroga p where 1=1");
        if (prorroga.getEsAceptado() != null) {
            sql.append(" and p.esAceptado=:aceptado");
            parametros.put("aceptado", prorroga.getEsAceptado());
            existeFiltro = Boolean.TRUE;
        }
        if (prorroga.getEsActivo() != null) {
            sql.append(" and p.esActivo=:activo");
            parametros.put("activo", prorroga.getEsActivo());
            existeFiltro = Boolean.TRUE;
        }
        if (prorroga.getCronogramaId() != null) {
            sql.append(" and p.cronogramaId=:cronograma");
            parametros.put("cronograma", prorroga.getCronogramaId());
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

    @Override
    public Prorroga obtenerPorFechaMayor(final Prorroga prorroga) {
        @SuppressWarnings("UnusedAssignment")
        List<Prorroga> prorrogas = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("SELECT p FROM Prorroga p where p.id=(SELECT MAX(p.id) FROM Prorroga p and p.cronogramaId=:cronograma  and p.esActivo=:activo)");
        parametros.put("activo", prorroga.getEsActivo());
        parametros.put("cronograma", prorroga.getCronogramaId());
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        prorrogas = q.getResultList();
        if (prorrogas == null) {
            return null;
        }
        return !prorrogas.isEmpty() ? prorrogas.get(0) : null;
    }

}
