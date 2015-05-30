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
public class AreaAcademicaOntDTO implements Serializable {

    private Integer id;
    private String institucion;
    private String nombre;
    private String sigla;
    private Individual individual;
    private String uri;

    public AreaAcademicaOntDTO() {
    }

    public AreaAcademicaOntDTO(Integer id, String institucion, String nombre, String sigla,String uri) {
        this.id = id;
        this.institucion = institucion;
        this.nombre = nombre;
        this.sigla = sigla;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getInstitucion() {
        return institucion;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
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
