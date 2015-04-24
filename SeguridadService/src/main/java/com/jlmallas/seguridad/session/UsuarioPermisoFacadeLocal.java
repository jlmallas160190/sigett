/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.seguridad.session;

import com.jlmallas.seguridad.entity.UsuarioPermiso;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface UsuarioPermisoFacadeLocal {

    void create(UsuarioPermiso usuarioPermiso);

    void edit(UsuarioPermiso usuarioPermiso);

    void remove(UsuarioPermiso usuarioPermiso);

    UsuarioPermiso find(Object id);

    List<UsuarioPermiso> findAll();

    List<UsuarioPermiso> findRange(int[] range);

    List<UsuarioPermiso> buscarPorUsuario(Long usuarioId);

    UsuarioPermiso buscarPorUsuarioYCodigoPermiso(Long usuarioId, String codigoPermiso);

    int count();

}
