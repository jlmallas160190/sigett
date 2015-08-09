/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.seguridad.service;

import java.util.List;
import javax.ejb.Local;
import org.jlmallas.seguridad.entity.Rol;

/**
 *
 * @author jorge-luis
 */
@Local
public interface RolService {

    void guardar(final Rol rol);

    void actualizar(final Rol rol);

    void eliminar(final Rol rol);

    Rol buscarPorId(final Rol rol);

    List<Rol> buscar(final Rol rol);
}
