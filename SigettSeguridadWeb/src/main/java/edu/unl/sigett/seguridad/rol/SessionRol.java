/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.rol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;

import javax.inject.Named;
import org.jlmallas.seguridad.entity.Permiso;
import org.jlmallas.seguridad.entity.Rol;
import org.jlmallas.seguridad.entity.RolPermiso;
import org.primefaces.model.DualListModel;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionRol implements Serializable {

    private List<Rol> roles;
    private List<RolPermiso> rolPermisosGrabar;

    private DualListModel<Permiso> permisosDualList;
    private List<RolPermiso> rolPermisosRemovidos;
    private int numeroPermisosDisponibles = 0;
    private int numeroPermisosSeleccionados = 0;

    private Rol rol;

    public SessionRol() {
        this.rolPermisosGrabar=new ArrayList<>();
        this.rolPermisosRemovidos=new ArrayList<>();
        this.roles = new ArrayList<>();
        this.rol = new Rol();
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    public DualListModel<Permiso> getPermisosDualList() {
        return permisosDualList;
    }

    public void setPermisosDualList(DualListModel<Permiso> permisosDualList) {
        this.permisosDualList = permisosDualList;
    }

    public List<RolPermiso> getRolPermisosRemovidos() {
        return rolPermisosRemovidos;
    }

    public void setRolPermisosRemovidos(List<RolPermiso> rolPermisosRemovidos) {
        this.rolPermisosRemovidos = rolPermisosRemovidos;
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

    public List<RolPermiso> getRolPermisosGrabar() {
        return rolPermisosGrabar;
    }

    public void setRolPermisosGrabar(List<RolPermiso> rolPermisosGrabar) {
        this.rolPermisosGrabar = rolPermisosGrabar;
    }

}
