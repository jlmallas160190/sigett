/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.pertinencia;

import edu.unl.sigett.entity.Pertinencia;
import edu.unl.sigett.util.DocumentoCarreraDTO;
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
public class PertinenciaDM implements Serializable {

    private Pertinencia pertinencia;
    private DocumentoCarreraDTO documentoCarreraDTO;

    private List<Pertinencia> pertinencias;
    private List<Pertinencia> filterPertinencias;

    private Boolean renderedPanelCrud;
    private Boolean renderedMediaInforme;
    private Boolean renderedCrud;
    private Boolean renderedCrudCronograma;

    public PertinenciaDM() {
        this.pertinencias = new ArrayList<>();
        this.filterPertinencias = new ArrayList<>();
        this.pertinencia = new Pertinencia();
    }

    public Pertinencia getPertinencia() {
        return pertinencia;
    }

    public void setPertinencia(Pertinencia pertinencia) {
        this.pertinencia = pertinencia;
    }

    public List<Pertinencia> getPertinencias() {
        return pertinencias;
    }

    public void setPertinencias(List<Pertinencia> pertinencias) {
        this.pertinencias = pertinencias;
    }

    public List<Pertinencia> getFilterPertinencias() {
        return filterPertinencias;
    }

    public void setFilterPertinencias(List<Pertinencia> filterPertinencias) {
        this.filterPertinencias = filterPertinencias;
    }

    public Boolean getRenderedPanelCrud() {
        return renderedPanelCrud;
    }

    public void setRenderedPanelCrud(Boolean renderedPanelCrud) {
        this.renderedPanelCrud = renderedPanelCrud;
    }

    public Boolean getRenderedMediaInforme() {
        return renderedMediaInforme;
    }

    public void setRenderedMediaInforme(Boolean renderedMediaInforme) {
        this.renderedMediaInforme = renderedMediaInforme;
    }

    public DocumentoCarreraDTO getDocumentoCarreraDTO() {
        return documentoCarreraDTO;
    }

    public void setDocumentoCarreraDTO(DocumentoCarreraDTO documentoCarreraDTO) {
        this.documentoCarreraDTO = documentoCarreraDTO;
    }

    public Boolean getRenderedCrud() {
        return renderedCrud;
    }

    public void setRenderedCrud(Boolean renderedCrud) {
        this.renderedCrud = renderedCrud;
    }

    public Boolean getRenderedCrudCronograma() {
        return renderedCrudCronograma;
    }

    public void setRenderedCrudCronograma(Boolean renderedCrudCronograma) {
        this.renderedCrudCronograma = renderedCrudCronograma;
    }

}
