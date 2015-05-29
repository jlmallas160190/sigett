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
public class CarreraOntDTO implements Serializable {

    private Long id;
    private String nombre;
    private String sigla;
    private NivelAcademicoOntDTO nivelAcademicoDTO;
    private AreaAcademicaOntDTO areaAcademicaDTO;
    private Individual individual;

    public CarreraOntDTO(Long id, String nombre, String sigla, NivelAcademicoOntDTO nivelDTO, AreaAcademicaOntDTO areaAcademicaDTO) {
        this.id = id;
        this.sigla = sigla;
        this.nombre = nombre;
        this.nivelAcademicoDTO = nivelDTO;
        this.areaAcademicaDTO = areaAcademicaDTO;
    }

    public CarreraOntDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public NivelAcademicoOntDTO getNivelAcademicoDTO() {
        return nivelAcademicoDTO;
    }

    public void setNivelAcademicoDTO(NivelAcademicoOntDTO nivelAcademicoDTO) {
        this.nivelAcademicoDTO = nivelAcademicoDTO;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public AreaAcademicaOntDTO getAreaAcademicaDTO() {
        return areaAcademicaDTO;
    }

    public void setAreaAcademicaDTO(AreaAcademicaOntDTO areaAcademicaDTO) {
        this.areaAcademicaDTO = areaAcademicaDTO;
    }

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

}
