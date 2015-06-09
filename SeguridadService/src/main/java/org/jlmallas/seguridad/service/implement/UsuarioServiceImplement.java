/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.seguridad.service.implement;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.jlmallas.seguridad.dao.PermisoDao;
import org.jlmallas.seguridad.dao.RolPermisoDao;
import org.jlmallas.seguridad.dao.RolUsuarioDao;
import org.jlmallas.seguridad.dao.UsuarioDao;
import org.jlmallas.seguridad.dao.UsuarioPermisoDao;
import org.jlmallas.seguridad.entity.Permiso;
import org.jlmallas.seguridad.entity.RolPermiso;
import org.jlmallas.seguridad.entity.RolUsuario;
import org.jlmallas.seguridad.entity.Usuario;
import org.jlmallas.seguridad.entity.UsuarioPermiso;
import org.jlmallas.seguridad.service.UsuarioService;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class UsuarioServiceImplement implements UsuarioService {

    @EJB
    private UsuarioPermisoDao usuarioPermisoDao;
    @EJB
    private PermisoDao permisoDao;
    @EJB
    private RolUsuarioDao rolUsuarioDao;
    @EJB
    private RolPermisoDao rolPermisoDao;
    @EJB
    private UsuarioDao usuarioDao;

    @Override
    public Integer logear(String username, String password) {
        Integer var = 0;
        Usuario usuarioPorUsername = usuarioDao.buscarPorUsuario(username);
        Usuario usuarioClave = new Usuario();
        if (usuarioPorUsername != null) {
            usuarioClave = usuarioDao.buscarPorClave(usuarioPorUsername.getUsername(), password);
        }
        if (usuarioPorUsername != null) {
            if (usuarioClave != null) {
                if (usuarioPorUsername.equals(usuarioClave)) {
                    var = 1;
                } else {
                    var = 3;
                }
            } else {
                var = 2;
            }
        }
        return var;
    }

    @Override
    public Boolean unicoUsername(String username) {
        Boolean var = Boolean.FALSE;
        List<Usuario> usuarios = usuarioDao.buscarPorCriterio(new Usuario(Long.MIN_VALUE, null, username, null, null, null, true, true));
        Usuario user = !usuarios.isEmpty() ? usuarios.get(0) : null;
        if (user != null) {
            var = Boolean.TRUE;
        }
        return var;
    }

    @Override
    public Integer tienePermiso(Usuario usuario, String permiso) {
        if (usuario.getId() == null) {
            return 0;
        }
        if (usuario.getEsSuperuser()) {
            return 1;
        }
        List<Permiso> permisos = permisoDao.buscar(new Permiso(permiso, null));
        List<UsuarioPermiso> usuarioPermisos = usuarioPermisoDao.buscar(new UsuarioPermiso(usuario, !permisos.isEmpty() ? permisos.get(0) : null));
        UsuarioPermiso up = !usuarioPermisos.isEmpty() ? usuarioPermisos.get(0) : null;
        if (up != null) {
            return 1;
        }
        List<RolUsuario> rolUsuarios = rolUsuarioDao.buscar(new RolUsuario(usuario, null));
        for (RolUsuario rolUsuario : rolUsuarios) {
            List<RolPermiso> rolPermisos = rolPermisoDao.buscar(new RolPermiso(rolUsuario.getRolId(), !permisos.isEmpty() ? permisos.get(0) : null));
            RolPermiso rp = !rolPermisos.isEmpty() ? rolPermisos.get(0) : null;
            if (rp != null) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public void guardar(Usuario usuario) {
        this.usuarioDao.create(usuario);
    }

    @Override
    public void actualizar(Usuario usuario) {
        this.usuarioDao.edit(usuario);
    }

    @Override
    public void eliminar(Usuario usuario) {
        this.usuarioDao.remove(usuario);
    }

    @Override
    public Usuario buscarPorId(Usuario usuario) {
        return this.usuarioDao.find(usuario.getId());
    }

    @Override
    public List<Usuario> buscar(final Usuario usuario) {
        return this.usuarioDao.buscarPorCriterio(usuario);
    }

}
