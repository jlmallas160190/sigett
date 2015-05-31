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

    private Double tamanioArchivo;

    private List<Item> catalogosDocumento;

    public SessionDocumentoProyecto() {
        this.catalogoSeleccionado = new Item();
        this.catalogosDocumento = new ArrayList<>();
        this.documentoProyectoDTOSeleccionado = new DocumentoProyectoDTO();
    }

    public DocumentoProyectoDTO getDocumentoProyectoDTOSeleccionado() {
        return documentoProyectoDTOSeleccionado;
    }

    public void setDocumentoProyectoDTOSeleccionado(DocumentoProyectoDTO documentoProyectoDTOSeleccionado) {
        this.documentoProyectoDTOSeleccionado = documentoProyectoDTOSeleccionado;
    }

    public List<Item> getCatalogosDocumento() {
        return catalogosDocumento;
    }

    public void setCatalogosDocumento(List<Item> catalogosDocumento) {
        this.catalogosDocumento = catalogosDocumento;
    }

    public Item getCatalogoSeleccionado() {
        return catalogoSeleccionado;
    }

    public void setCatalogoSeleccionado(Item catalogoSeleccionado) {
        this.catalogoSeleccionado = catalogoSeleccionado;
    }

    public Double getTamanioArchivo() {
        return tamanioArchivo;
    }

    public void setTamanioArchivo(Double tamanioArchivo) {
        this.tamanioArchivo = tamanioArchivo;
    }

}
