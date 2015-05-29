/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.seguridad.dao;

import org.jlmallas.seguridad.entity.UsuarioPermiso;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface UsuarioPermisoDao {

    void create(UsuarioPermiso usuarioPermiso);

    void edit(UsuarioPermiso usuarioPermiso);

    void remove(UsuarioPermiso usuarioPermiso);

    UsuarioPermiso find(Object id);

    List<UsuarioPermiso> findAll();

    List<UsuarioPermiso> findRange(int[] range);

    List<UsuarioPermiso> buscarPorUsuario(Long usuarioId);

    UsuarioPermiso buscarPorUsuarioYCodigoPermiso(Long usuarioId, String codigoPermiso);

    List<UsuarioPermiso> buscar(final UsuarioPermiso usuarioPermiso);

    int count();

}
