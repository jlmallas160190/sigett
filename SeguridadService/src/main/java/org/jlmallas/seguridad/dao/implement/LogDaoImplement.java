/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.seguridad.dao.implement;

import org.jlmallas.seguridad.entity.Log;
import org.jlmallas.seguridad.entity.Usuario;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.jlmallas.seguridad.dao.AbstractDao;
import org.jlmallas.seguridad.dao.LogDao;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class LogDaoImplement extends AbstractDao<Log> implements LogDao {

    public LogDaoImplement() {
        super(Log.class);
    }

    @Override
    public Log crearLog(String tabla, String tablaId, String accion, String informe, Usuario usuario) {
        Calendar fecha = Calendar.getInstance();
        Log log = new Log();
        log.setAccion(accion);
        log.setHoraAccion(fecha.getTime());
        log.setFechaAccion(fecha.getTime());
        log.setInforme(informe);
        log.setTabla(tabla);
        log.setTablaId(tablaId);
        log.setUsuarioId(usuario);
        return log;
    }

    @Override
    public List<Log> buscarPorTablaId(String nombreTabla, String tablaId) {
        try {
            Query query = em.createQuery("Select l from Log l where" + " (l.tabla=:tabla and l.tablaId=:tablaId) order by l.fechaAccion DESC");
            query.setParameter("tabla", nombreTabla);
            query.setParameter("tablaId", tablaId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<Log> buscarPorTablaFecha(String tabla, Date fechaInicio, Date fechaFin) {
        try {
            Query query = em.createQuery("Select l from Log l where " + " (l.tabla=:tabla and  l.fechaAccion BETWEEN(:fechaInicio) and (:fechaFin)) order by l.fechaAccion  DESC");
            query.setParameter("tabla", tabla);
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
