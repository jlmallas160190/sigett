/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.seguridad.service;

import java.util.List;
import javax.ejb.Local;
import org.jlmallas.seguridad.entity.Usuario;

/**
 *
 * @author jorge-luis
 */
@Local
public interface UsuarioService {

    void guardar(final Usuario usuario);

    void actualizar(final Usuario usuario);

    void eliminar(final Usuario usuario);

    Usuario buscarPorId(final Usuario usuario);

    List<Usuario> buscar(final Usuario usuario);

    Integer logear(final String username, final String password);

    Boolean unicoUsername(final String username);

    Integer tienePermiso(final Usuario usuario, final String permiso);
}
