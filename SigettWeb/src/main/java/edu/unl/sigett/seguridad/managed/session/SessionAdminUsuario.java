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
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

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
@ConversationScoped
public class SessionAdminUsuario implements Serializable {

    @Inject
    private Conversation conversation;

    private Usuario usuario;
    private boolean visualizarEditar;
    private String confirmarClave;
    private DualListModel<Rol> rolesDualList;
    private DualListModel<Permiso> permisosDualList;
    private DualListModel<Carrera> carrerasDualList;
    private List<RolUsuario> rolesUsuariosRemovidos;
    private List<UsuarioCarrera> usuariosCarrerasRemovidas;
    private List<UsuarioPermiso> usuariosPermisoRemovidos;
    private List<Usuario> usuarios;
    private int numeroCarrerasDisponibles = 0;
    private int numeroCarrerasSeleccionadas = 0;
    private int numeroPermisosDisponibles = 0;
    private int numeroPermisosSeleccionados = 0;
    private String confirmarClaveEstudiante;
    private String criterio;
    private String confirmaClave;

    private boolean renderedNoEditar;
    private boolean renderedEditar;
    private boolean renderedCrear;

    private boolean renderedSincronizar;

    @PostConstruct
    public void init() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
    }

    public SessionAdminUsuario() {
        this.visualizarEditar = false;
        this.confirmarClave = "";
        this.usuario = new Usuario();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean isVisualizarEditar() {
        return visualizarEditar;
    }

    public void setVisualizarEditar(boolean visualizarEditar) {
        this.visualizarEditar = visualizarEditar;
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

    public String getConfirmaClave() {
        return confirmaClave;
    }

    public void setConfirmaClave(String confirmaClave) {
        this.confirmaClave = confirmaClave;
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

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

}
