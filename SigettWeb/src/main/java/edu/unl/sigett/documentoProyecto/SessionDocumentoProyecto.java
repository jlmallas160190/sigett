/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.documentoProyecto;

import com.jlmallas.comun.entity.Item;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionDocumentoProyecto implements Serializable {

    private DocumentoProyectoDTO documentoProyectoDTOSeleccionado;
    private Item catalogoSeleccionado;

    private List<Item> catalogosDocuemento;

    public SessionDocumentoProyecto() {
        this.catalogoSeleccionado = new Item();
        this.catalogosDocuemento = new ArrayList<>();
        this.documentoProyectoDTOSeleccionado = new DocumentoProyectoDTO();
    }

    public DocumentoProyectoDTO getDocumentoProyectoDTOSeleccionado() {
        return documentoProyectoDTOSeleccionado;
    }

    public void setDocumentoProyectoDTOSeleccionado(DocumentoProyectoDTO documentoProyectoDTOSeleccionado) {
        this.documentoProyectoDTOSeleccionado = documentoProyectoDTOSeleccionado;
    }

    public List<Item> getCatalogosDocuemento() {
        return catalogosDocuemento;
    }

    public void setCatalogosDocuemento(List<Item> catalogosDocuemento) {
        this.catalogosDocuemento = catalogosDocuemento;
    }

    public Item getCatalogoSeleccionado() {
        return catalogoSeleccionado;
    }

    public void setCatalogoSeleccionado(Item catalogoSeleccionado) {
        this.catalogoSeleccionado = catalogoSeleccionado;
    }

}
