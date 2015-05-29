/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.seguridad.dao;

import org.jlmallas.seguridad.entity.RolUsuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface RolUsuarioDao {

    void create(RolUsuario rolUsuario);

    void edit(RolUsuario rolUsuario);

    void remove(RolUsuario rolUsuario);

    RolUsuario find(Object id);

    List<RolUsuario> findAll();

    List<RolUsuario> findRange(int[] range);

    List<RolUsuario> buscarPorUsuario(Long usuarioId);

    List<RolUsuario> buscar(final RolUsuario rolUsuario);

    int count();

}
