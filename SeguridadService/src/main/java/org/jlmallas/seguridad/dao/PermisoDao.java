/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.seguridad.dao;

import org.jlmallas.seguridad.entity.Permiso;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface PermisoDao {

    void create(Permiso permiso);

    void edit(Permiso permiso);

    void remove(Permiso permiso);

    Permiso find(Object id);

    List<Permiso> findAll();

    List<Permiso> findRange(int[] range);

    int count();

    List<Permiso> buscar(final Permiso permiso);
}
