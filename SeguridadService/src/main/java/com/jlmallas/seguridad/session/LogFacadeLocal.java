/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.seguridad.session;

import com.jlmallas.seguridad.entity.Log;
import com.jlmallas.seguridad.entity.Usuario;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface LogFacadeLocal {

    void create(Log log);

    void edit(Log log);

    void remove(Log log);

    Log find(Object id);

    List<Log> findAll();

    List<Log> findRange(int[] range);

    int count();

    List<Log> buscarPorTablaId(String nombreTabla, String tablaId);

    List<Log> buscarPorTablaFecha(String tabla, Date fecha, Date fechaFin);

    Log crearLog(String tabla, String tablaId, String accion, String informe, Usuario usuario);

}
