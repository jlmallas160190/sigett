/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.lud.dto;

import com.hp.hpl.jena.ontology.Individual;
import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class LineaInvestigacionProyectoOntDTO implements Serializable {

    private Long id;
    private LineaInvestigacionOntDTO lineaInvestigacionDTO;
    private ProyectoOntDTO proyectoDTO;
    private Individual individual;
    private String uri;

    public LineaInvestigacionProyectoOntDTO() {
    }

    public LineaInvestigacionProyectoOntDTO(Long id, LineaInvestigacionOntDTO lineaInvestigacionDTO, ProyectoOntDTO proyectoDTO, String uri) {
        this.id = id;
        this.lineaInvestigacionDTO = lineaInvestigacionDTO;
        this.proyectoDTO = proyectoDTO;
        this.uri = uri;
    }

    public LineaInvestigacionOntDTO getLineaInvestigacionDTO() {
        return lineaInvestigacionDTO;
    }

    public void setLineaInvestigacionDTO(LineaInvestigacionOntDTO lineaInvestigacionDTO) {
        this.lineaInvestigacionDTO = lineaInvestigacionDTO;
    }

    public ProyectoOntDTO getProyectoDTO() {
        return proyectoDTO;
    }

    public void setProyectoDTO(ProyectoOntDTO proyectoDTO) {
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
