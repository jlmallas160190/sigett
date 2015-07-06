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
import org.jlmallas.seguridad.dao.PermisoDao;
import org.jlmallas.seguridad.dao.RolPermisoDao;
import org.jlmallas.seguridad.dao.RolUsuarioDao;
import org.jlmallas.seguridad.dao.UsuarioDao;
import org.jlmallas.seguridad.dao.UsuarioPermisoDao;
import org.jlmallas.seguridad.entity.Permiso;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class UsuarioDaoImplement extends AbstractDao<Usuario> implements UsuarioDao {

    @EJB
    private RolPermisoDao rolPermisoDao;
    @EJB
    private RolUsuarioDao rolUsuarioDao;
    @EJB
    private PermisoDao permisoDao;
    @EJB
    private UsuarioPermisoDao usuarioPermisoDao;

    public UsuarioDaoImplement() {
        super(Usuario.class);
    }

    @Override
    public List<Usuario> buscarPorCriterio(Usuario usuario) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("Select u from Usuario u where 1=1 ");
        if (usuario.getNombres() != null) {
            sql.append(" and (LOWER(u.nombres) like concat('%',LOWER(:nombres),'%'))");
            parametros.put("nombres", usuario.getNombres());
            existeFiltro = Boolean.TRUE;
        }
        if (usuario.getApellidos() != null) {
            sql.append(" and (LOWER(u.apellidos) like concat('%',LOWER(:apellidos),'%'))");
            parametros.put("apellidos", usuario.getApellidos());
            existeFiltro = Boolean.TRUE;
        }
        if (usuario.getUsername() != null) {
            sql.append(" and u.username=:username");
            parametros.put("username", usuario.getUsername());
            existeFiltro = Boolean.TRUE;
        }
        if (usuario.getPassword() != null) {
            sql.append(" and u.password=:password");
            parametros.put("password", usuario.getPassword());
            existeFiltro = Boolean.TRUE;
        }
        if (usuario.getEsActivo() != null) {
            sql.append(" and u.esActivo=:activo");
            parametros.put("activo", usuario.getEsActivo());
            existeFiltro = Boolean.TRUE;
        }
        if (!existeFiltro) {
            return new ArrayList<>();
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
        if (user.getId() == null) {
            return 0;
        }
        if (user.getEsSuperuser()) {
            return 1;
        }
        List<Permiso> permisos = permisoDao.buscar(new Permiso(permiso, null));
        List<UsuarioPermiso> usuarioPermisos = usuarioPermisoDao.buscar(new UsuarioPermiso(user, !permisos.isEmpty() ? permisos.get(0) : null));
        UsuarioPermiso up = !usuarioPermisos.isEmpty() ? usuarioPermisos.get(0) : null;
        if (up != null) {
            return 1;
        }
        List<RolUsuario> rolUsuarios = rolUsuarioDao.buscar(new RolUsuario(user, null));
        for (RolUsuario rolUsuario : rolUsuarios) {
            List<RolPermiso> rolPermisos = rolPermisoDao.buscar(new RolPermiso(rolUsuario.getRolId(), !permisos.isEmpty() ? permisos.get(0) : null));
            RolPermiso rp = !rolPermisos.isEmpty() ? rolPermisos.get(0) : null;
            if (rp != null) {
                return 1;
            }
        }
        return 0;
    }

}
