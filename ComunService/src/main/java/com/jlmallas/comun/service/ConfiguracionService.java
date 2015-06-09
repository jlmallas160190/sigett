/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.service;

import com.jlmallas.comun.entity.Configuracion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface ConfiguracionService {

    void guardar(final Configuracion configuracion);

    void actualizar(final Configuracion configuracion);

    void eliminar(final Configuracion configuracion);

    Configuracion buscarPorId(final Configuracion configuracion);

    List<Configuracion> buscar(final Configuracion configuracion);
}
