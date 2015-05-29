/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.seguridad.dao;

import org.jlmallas.seguridad.entity.RolPermiso;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface RolPermisoDao {

    void create(RolPermiso rolPermiso);

    void edit(RolPermiso rolPermiso);

    void remove(RolPermiso rolPermiso);

    RolPermiso find(Object id);

    List<RolPermiso> findAll();

    List<RolPermiso> findRange(int[] range);

    List<RolPermiso> buscarPorRol(Long rolId);

    RolPermiso buscarPorRolCodigoPermiso(Long rolId, String codigoNombre);

    List<RolPermiso> buscar(final RolPermiso rolPermiso);

    int count();

}
