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
public class LineaInvestigacionProyectoDTO implements Serializable{
    private LineaInvestigacionDTO lineaInvestigacionDTO;
    private ProyectoDTO proyectoDTO;

    public LineaInvestigacionProyectoDTO() {
    }

    public LineaInvestigacionProyectoDTO(LineaInvestigacionDTO lineaInvestigacionDTO, ProyectoDTO proyectoDTO) {
        this.lineaInvestigacionDTO = lineaInvestigacionDTO;
        this.proyectoDTO = proyectoDTO;
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
    
}
