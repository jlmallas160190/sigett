/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.util;

import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.service.ItemService;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.jlmallas.academico.service.DocenteCarreraService;
import edu.unl.sigett.docenteUsuario.DocenteUsuarioDM;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Inject;

/**
 *
 * @author jorge-luis
 */
@Named(value = "selectItemsController")
@SessionScoped
public class SelectItemsController implements Serializable {

    @Inject
    private DocenteUsuarioDM docenteUsuarioDM;
    @Inject
    private SessionSelectItems sessionSelectItems;
    @EJB
    private ItemService itemService;
    @EJB
    private DocenteCarreraService docenteCarreraService;

    public SelectItemsController() {

    }

    public void preRenderView() {
        this.listadoCategorias();
        this.listadoEstados();
        this.listadoTipos();
        listadoDocenteCarreras();
    }

    private void listadoDocenteCarreras() {
        this.sessionSelectItems.getDocenteCarreras().addAll(docenteCarreraService.buscar(new DocenteCarrera(null, docenteUsuarioDM.getDocenteUsuarioDTO().getDocente(), null, Boolean.TRUE)));
    }

    private void listadoTipos() {
        this.sessionSelectItems.getTipos().clear();
        this.sessionSelectItems.setTipos(itemService.buscarPorCatalogo(CatalogoEnum.TIPOPROYECTO.getTipo()));
    }

    private void listadoCategorias() {
        this.sessionSelectItems.getCategorias().clear();
        this.sessionSelectItems.setCategorias(itemService.buscarPorCatalogo(CatalogoEnum.CATALOGOPROYECTO.getTipo()));
    }

    private void listadoEstados() {
        this.sessionSelectItems.getEstados().clear();
        this.sessionSelectItems.setEstados(itemService.buscarPorCatalogo(CatalogoEnum.ESTADOPROYECTO.getTipo()));
    }

}
