/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.service;

import com.jlmallas.comun.entity.Nacionalidad;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface NacionalidadService {

    void guardar(final Nacionalidad nacionalidad);

    void actualizar(final Nacionalidad nacionalidad);

    void eliminar(final Nacionalidad nacionalidad);

    Nacionalidad buscarPorId(final Nacionalidad nacionalidad);

    List<Nacionalidad> buscar(final Nacionalidad nacionalidad);
}
