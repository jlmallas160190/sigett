/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.seguridad.dao;

import org.jlmallas.seguridad.entity.Usuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface UsuarioDao {

    void create(Usuario usuario);

    void edit(Usuario usuario);

    void remove(Usuario usuario);

    Usuario find(Object id);

    List<Usuario> findAll();

    List<Usuario> findRange(int[] range);

    int count();

    Usuario buscarPorClave(String username, String password);

    Usuario buscarPorUsuario(String username);

    List<Usuario> buscarPorCriterio(Usuario usuario);

    int logear(String username, String password);

    boolean unicoUsername(String username);

    int tienePermiso(Usuario usuario, String permiso);
}
