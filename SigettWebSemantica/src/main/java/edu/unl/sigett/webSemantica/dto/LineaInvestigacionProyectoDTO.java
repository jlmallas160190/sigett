/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.webSemantica.dto;

import com.hp.hpl.jena.ontology.Individual;
import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class LineaInvestigacionProyectoDTO implements Serializable {

    private Long id;
    private LineaInvestigacionDTO lineaInvestigacionDTO;
    private ProyectoDTO proyectoDTO;
    private Individual individual;
    private String uri;

    public LineaInvestigacionProyectoDTO() {
    }

    public LineaInvestigacionProyectoDTO(Long id, LineaInvestigacionDTO lineaInvestigacionDTO, ProyectoDTO proyectoDTO, String uri) {
        this.id = id;
        this.lineaInvestigacionDTO = lineaInvestigacionDTO;
        this.proyectoDTO = proyectoDTO;
        this.uri = uri;
    }

    public LineaInvestigacionDTO getLineaInvestigacionDTO() {
        return lineaInvestigacionDTO;
    }

    public void setLineaInvestigacionDTO(LineaInvestigacionDTO lineaInvestigacionDTO) {
        this.lineaInvestigacionDTO = lineaInvestigacionDTO;
    }

    public ProyectoDTO getProyectoDTO() {
        return proyectoDTO;
    }

    public void setProyectoDTO(ProyectoDTO proyectoDTO) {
        this.proyectoDTO = proyectoDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
