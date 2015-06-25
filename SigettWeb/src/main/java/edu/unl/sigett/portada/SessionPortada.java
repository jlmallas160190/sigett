/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.portada;

import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.webSemantica.dto.ProyectoOntDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author jorge-luis
 */
@Named(value = "sessionPortada")
@SessionScoped
public class SessionPortada implements Serializable{

    private ProyectoOntDTO proyectoOntDTOSeleccionado;
    private Proyecto proyectoSeleccionado;
    
    private List<ProyectoOntDTO> proyectosOntology;
    
    private String filtroBuscadorSemantico;

    public SessionPortada() {
        this.proyectosOntology = new ArrayList<>();
    }

    public ProyectoOntDTO getProyectoOntDTOSeleccionado() {
        return proyectoOntDTOSeleccionado;
    }

    public void setProyectoOntDTOSeleccionado(ProyectoOntDTO proyectoOntDTOSeleccionado) {
        this.proyectoOntDTOSeleccionado = proyectoOntDTOSeleccionado;
    }

    public List<ProyectoOntDTO> getProyectosOntology() {
        return proyectosOntology;
    }

    public void setProyectosOntology(List<ProyectoOntDTO> proyectosOntology) {
        this.proyectosOntology = proyectosOntology;
    }

    public Proyecto getProyectoSeleccionado() {
        return proyectoSeleccionado;
    }

    public void setProyectoSeleccionado(Proyecto proyectoSeleccionado) {
        this.proyectoSeleccionado = proyectoSeleccionado;
    }

    public String getFiltroBuscadorSemantico() {
        return filtroBuscadorSemantico;
    }

    public void setFiltroBuscadorSemantico(String filtroBuscadorSemantico) {
        this.filtroBuscadorSemantico = filtroBuscadorSemantico;
    }

}
