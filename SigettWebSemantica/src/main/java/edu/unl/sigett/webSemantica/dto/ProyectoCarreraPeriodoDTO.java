/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.webSemantica.dto;

import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class ProyectoCarreraPeriodoDTO implements Serializable {

    private OfertaAcademicaDTO ofertaAcademicaDTO;
    private ProyectoDTO proyectoDTO;
    private CarreraDTO carreraDTO;

    public ProyectoCarreraPeriodoDTO(OfertaAcademicaDTO ofertaAcademicaDTO, ProyectoDTO proyectoDTO, CarreraDTO carreraDTO) {
        this.ofertaAcademicaDTO = ofertaAcademicaDTO;
        this.proyectoDTO = proyectoDTO;
        this.carreraDTO = carreraDTO;
    }

    public ProyectoCarreraPeriodoDTO() {
    }

    public OfertaAcademicaDTO getOfertaAcademicaDTO() {
        return ofertaAcademicaDTO;
    }

    public void setOfertaAcademicaDTO(OfertaAcademicaDTO ofertaAcademicaDTO) {
        this.ofertaAcademicaDTO = ofertaAcademicaDTO;
    }

    public ProyectoDTO getProyectoDTO() {
        return proyectoDTO;
    }

    public void setProyectoDTO(ProyectoDTO proyectoDTO) {
        this.proyectoDTO = proyectoDTO;
    }

    public CarreraDTO getCarreraDTO() {
        return carreraDTO;
    }

    public void setCarreraDTO(CarreraDTO carreraDTO) {
        this.carreraDTO = carreraDTO;
    }

}
