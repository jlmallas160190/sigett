/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.seguridad.dao.implement;

import org.jlmallas.seguridad.entity.RolPermiso;
import org.jlmallas.seguridad.entity.RolUsuario;
import org.jlmallas.seguridad.entity.Usuario;
import org.jlmallas.seguridad.entity.UsuarioPermiso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.jlmallas.seguridad.dao.AbstractDao;
import org.jlmallas.seguridad.dao.RolPermisoDao;
import org.jlmallas.seguridad.dao.RolUsuarioDao;
import org.jlmallas.seguridad.dao.UsuarioDao;
import org.jlmallas.seguridad.dao.UsuarioPermisoDao;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class UsuarioDaoImplement extends AbstractDao<Usuario> implements UsuarioDao {

    @EJB
    private UsuarioPermisoDao usuarioPermisoFacadeLocal;
    @EJB
    private RolPermisoDao rolPermisoFacadeLocal;
    @EJB
    private RolUsuarioDao rolUsuarioFacadeLocal;

    public UsuarioDaoImplement() {
        super(Usuario.class);
    }

    @Override
    public boolean unicoUsername(String username) {
        boolean var = false;
        Usuario user = buscarPorUsuario(username);
        if (user != null) {
            var = true;
        }
        return var;
    }

    @Override
    public int logear(String username, String password) {
        //if var=0 no existe clave; if var=2 no existe username; if var=3 no coinciden
        int var = 0;
        Usuario usuarioPorUsername = buscarPorUsuario(username);
        Usuario usuarioClave = new Usuario();
        if (usuarioPorUsername != null) {
            usuarioClave = buscarPorClave(usuarioPorUsername.getUsername(), password);
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
    public List<Usuario> buscarPorCriterio(Usuario usuario) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("Select u from Usuario u where 1=1 ");
        if (usuario.getNombres() != null) {
            sql.append(" and (LOWER(u.nombres) like concat('%',LOWER(:nombres),'%'))");
            parametros.put("nombres", usuario.getNombres());
        }
        if (usuario.getApellidos() != null) {
            sql.append(" and (LOWER(u.apellidos) like concat('%',LOWER(:apellidos),'%'))");
            parametros.put("apellidos", usuario.getNombres());
        }
        if (usuario.getNombres() != null) {
            sql.append(" and (LOWER(u.nombres) like concat('%',LOWER(:nombres),'%'))");
            parametros.put("nombres", usuario.getNombres());
        }
        sql.append(" order by u.apellidos asc ");
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }

    @Override
    public Usuario buscarPorClave(String username, String password) {
        List<Usuario> usuarios = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT u FROM Usuario u WHERE " + "(u.password=:password and u.username=:username)");
            query.setParameter("password", password);
            query.setParameter("username", username);
            usuarios = query.getResultList();
            return !usuarios.isEmpty() ? usuarios.get(0) : null;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public Usuario buscarPorUsuario(String username) {
        List<Usuario> usuarios = new ArrayList<>();
        try {
            Query query = em.createNamedQuery("Usuario.findByUsername");
            query.setParameter("username", username);
            usuarios = query.getResultList();
            return !usuarios.isEmpty() ? usuarios.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public int tienePermiso(Usuario user, String permiso) {
        int var = 0;
        boolean permisoEncontrado = false;
        if (user.getId() != null) {
            if (user.getEsSuperuser()) {
                permisoEncontrado = true;
            } else {
                UsuarioPermiso up = usuarioPermisoFacadeLocal.buscarPorUsuarioYCodigoPermiso(user.getId(), permiso);
                if (up != null) {
                    permisoEncontrado = true;
                }
                if (permisoEncontrado == false) {
                    List<RolUsuario> rolUsuarios = rolUsuarioFacadeLocal.buscarPorUsuario(user.getId());
                    for (RolUsuario rolUsuario : rolUsuarios) {
                        RolPermiso rp = rolPermisoFacadeLocal.buscarPorRolCodigoPermiso(rolUsuario.getRolId().getId(), permiso);
                        if (rp != null) {
                            permisoEncontrado = true;
                            break;
                        }
                    }
                }
            }
            if (permisoEncontrado == true) {
                var = 1;
            } else {
                var = 2;
            }
        }
        return var;
    }

}
