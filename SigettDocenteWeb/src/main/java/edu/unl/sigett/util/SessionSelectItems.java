/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.util;

import com.jlmallas.comun.entity.Item;
import edu.jlmallas.academico.entity.DocenteCarrera;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author jorge-luis
 */
@Named(value = "sessionSelectItems")
@SessionScoped
public class SessionSelectItems implements Serializable {

    private List<Item> estados;
    private List<Item> categorias;
    private List<Item> tipos;
    private List<DocenteCarrera> docenteCarreras;

    public SessionSelectItems() {
        this.docenteCarreras=new ArrayList<>();
        this.estados = new ArrayList<>();
        this.categorias = new ArrayList<>();
        this.tipos = new ArrayList<>();
    }

    public List<Item> getEstados() {
        return estados;
    }

    public void setEstados(List<Item> estados) {
        this.estados = estados;
    }

    public List<Item> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Item> categorias) {
        this.categorias = categorias;
    }

    public List<Item> getTipos() {
        return tipos;
    }

    public void setTipos(List<Item> tipos) {
        this.tipos = tipos;
    }

    public List<DocenteCarrera> getDocenteCarreras() {
        return docenteCarreras;
    }

    public void setDocenteCarreras(List<DocenteCarrera> docenteCarreras) {
        this.docenteCarreras = docenteCarreras;
    }

}
