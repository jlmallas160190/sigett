/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.seguridad.session;

import com.jlmallas.seguridad.entity.Usuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface UsuarioFacadeLocal {

    void create(Usuario usuario);

    void edit(Usuario usuario);

    void remove(Usuario usuario);

    Usuario find(Object id);

    List<Usuario> findAll();

    List<Usuario> findRange(int[] range);

    int count();

    Usuario buscarPorClave(String username, String password);

    Usuario buscarPorUsuario(String username);

    List<Usuario> buscarPorCriterios(String criterio);

    int logear(String username, String password);

    boolean unicoUsername(String username);

    int tienePermiso(Usuario usuario, String permiso);
}
