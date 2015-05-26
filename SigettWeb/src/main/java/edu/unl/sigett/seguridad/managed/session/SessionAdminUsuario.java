/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.managed.session;

import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.entity.UsuarioCarrera;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;

import javax.inject.Named;
import org.jlmallas.seguridad.entity.Permiso;
import org.jlmallas.seguridad.entity.Rol;
import org.jlmallas.seguridad.entity.RolUsuario;
import org.jlmallas.seguridad.entity.UsuarioPermiso;
import org.primefaces.model.DualListModel;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionAdminUsuario implements Serializable {

    private Usuario usuario;
    private String confirmarClave;
    private DualListModel<Rol> rolesDualList;
    private DualListModel<Permiso> permisosDualList;
    private DualListModel<Carrera> carrerasDualList;
    private List<RolUsuario> rolesUsuariosRemovidos;
    private List<UsuarioCarrera> usuariosCarrerasRemovidas;
    private List<UsuarioPermiso> usuariosPermisoRemovidos;
    private List<Usuario> usuarios;
    private List<Usuario> filterUsuarios;
    private int numeroCarrerasDisponibles = 0;
    private int numeroCarrerasSeleccionadas = 0;
    private int numeroPermisosDisponibles = 0;
    private int numeroPermisosSeleccionados = 0;
    private String confirmarClaveEstudiante;
    private String criterio;

    private boolean renderedNoEditar;
    private boolean renderedEditar;
    private boolean renderedCrear;

    private boolean renderedSincronizar;

    public SessionAdminUsuario() {
        this.usuarios=new ArrayList<>();
        this.filterUsuarios=new ArrayList<>();
        this.confirmarClave = "";
        this.usuario = new Usuario();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getConfirmarClave() {
        return confirmarClave;
    }

    public void setConfirmarClave(String confirmarClave) {
        this.confirmarClave = confirmarClave;
    }

    public DualListModel<Rol> getRolesDualList() {
        return rolesDualList;
    }

    public void setRolesDualList(DualListModel<Rol> rolesDualList) {
        this.rolesDualList = rolesDualList;
    }

    public DualListModel<Permiso> getPermisosDualList() {
        return permisosDualList;
    }

    public void setPermisosDualList(DualListModel<Permiso> permisosDualList) {
        this.permisosDualList = permisosDualList;
    }

    public DualListModel<Carrera> getCarrerasDualList() {
        return carrerasDualList;
    }

    public void setCarrerasDualList(DualListModel<Carrera> carrerasDualList) {
        this.carrerasDualList = carrerasDualList;
    }

    public List<RolUsuario> getRolesUsuariosRemovidos() {
        return rolesUsuariosRemovidos;
    }

    public void setRolesUsuariosRemovidos(List<RolUsuario> rolesUsuariosRemovidos) {
        this.rolesUsuariosRemovidos = rolesUsuariosRemovidos;
    }

    public List<UsuarioCarrera> getUsuariosCarrerasRemovidas() {
        return usuariosCarrerasRemovidas;
    }

    public void setUsuariosCarrerasRemovidas(List<UsuarioCarrera> usuariosCarrerasRemovidas) {
        this.usuariosCarrerasRemovidas = usuariosCarrerasRemovidas;
    }

    public List<UsuarioPermiso> getUsuariosPermisoRemovidos() {
        return usuariosPermisoRemovidos;
    }

    public void setUsuariosPermisoRemovidos(List<UsuarioPermiso> usuariosPermisoRemovidos) {
        this.usuariosPermisoRemovidos = usuariosPermisoRemovidos;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public int getNumeroCarrerasDisponibles() {
        return numeroCarrerasDisponibles;
    }

    public void setNumeroCarrerasDisponibles(int numeroCarrerasDisponibles) {
        this.numeroCarrerasDisponibles = numeroCarrerasDisponibles;
    }

    public int getNumeroCarrerasSeleccionadas() {
        return numeroCarrerasSeleccionadas;
    }

    public void setNumeroCarrerasSeleccionadas(int numeroCarrerasSeleccionadas) {
        this.numeroCarrerasSeleccionadas = numeroCarrerasSeleccionadas;
    }

    public int getNumeroPermisosDisponibles() {
        return numeroPermisosDisponibles;
    }

    public void setNumeroPermisosDisponibles(int numeroPermisosDisponibles) {
        this.numeroPermisosDisponibles = numeroPermisosDisponibles;
    }

    public int getNumeroPermisosSeleccionados() {
        return numeroPermisosSeleccionados;
    }

    public void setNumeroPermisosSeleccionados(int numeroPermisosSeleccionados) {
        this.numeroPermisosSeleccionados = numeroPermisosSeleccionados;
    }

    public String getConfirmarClaveEstudiante() {
        return confirmarClaveEstudiante;
    }

    public void setConfirmarClaveEstudiante(String confirmarClaveEstudiante) {
        this.confirmarClaveEstudiante = confirmarClaveEstudiante;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }
    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public boolean isRenderedEditar() {
        return renderedEditar;
    }

    public void setRenderedEditar(boolean renderedEditar) {
        this.renderedEditar = renderedEditar;
    }

    public boolean isRenderedCrear() {
        return renderedCrear;
    }

    public void setRenderedCrear(boolean renderedCrear) {
        this.renderedCrear = renderedCrear;
    }

    public boolean isRenderedSincronizar() {
        return renderedSincronizar;
    }

    public void setRenderedSincronizar(boolean renderedSincronizar) {
        this.renderedSincronizar = renderedSincronizar;
    }

    public List<Usuario> getFilterUsuarios() {
        return filterUsuarios;
    }

    public void setFilterUsuarios(List<Usuario> filterUsuarios) {
        this.filterUsuarios = filterUsuarios;
    }

}
