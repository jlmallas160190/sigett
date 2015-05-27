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
public class CarreraDTO implements Serializable {

    private Long id;
    private String nombre;
    private String sigla;
    private NivelAcademicoDTO nivelAcademicoDTO;
    private AreaAcademicaDTO areaAcademicaDTO;
    private Individual individual;

    public CarreraDTO(Long id, String nombre, String sigla, NivelAcademicoDTO nivelDTO, AreaAcademicaDTO areaAcademicaDTO) {
        this.id = id;
        this.sigla = sigla;
        this.nombre = nombre;
        this.nivelAcademicoDTO = nivelDTO;
        this.areaAcademicaDTO = areaAcademicaDTO;
    }

    public CarreraDTO() {
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

    public NivelAcademicoDTO getNivelAcademicoDTO() {
        return nivelAcademicoDTO;
    }

    public void setNivelAcademicoDTO(NivelAcademicoDTO nivelAcademicoDTO) {
        this.nivelAcademicoDTO = nivelAcademicoDTO;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public AreaAcademicaDTO getAreaAcademicaDTO() {
        return areaAcademicaDTO;
    }

    public void setAreaAcademicaDTO(AreaAcademicaDTO areaAcademicaDTO) {
        this.areaAcademicaDTO = areaAcademicaDTO;
    }

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

}
