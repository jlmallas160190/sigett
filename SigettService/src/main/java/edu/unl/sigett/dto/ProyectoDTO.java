/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dto;

import edu.unl.sigett.entity.LineaInvestigacionProyecto;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.ProyectoCarreraOferta;
import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class ProyectoDTO implements Serializable{

    private Proyecto proyecto;
    private ProyectoCarreraOferta proyectoCarreraOferta;
    private LineaInvestigacionProyecto lineaInvestigacionProyecto;

    public ProyectoDTO(Proyecto proyecto, ProyectoCarreraOferta proyectoCarreraOferta, LineaInvestigacionProyecto lineaInvestigacionProyecto) {
        this.proyecto = proyecto;
        this.proyectoCarreraOferta = proyectoCarreraOferta;
        this.lineaInvestigacionProyecto = lineaInvestigacionProyecto;
    }

    public ProyectoDTO() {
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public ProyectoCarreraOferta getProyectoCarreraOferta() {
        return proyectoCarreraOferta;
    }

    public void setProyectoCarreraOferta(ProyectoCarreraOferta proyectoCarreraOferta) {
        this.proyectoCarreraOferta = proyectoCarreraOferta;
    }

    public LineaInvestigacionProyecto getLineaInvestigacionProyecto() {
        return lineaInvestigacionProyecto;
    }

    public void setLineaInvestigacionProyecto(LineaInvestigacionProyecto lineaInvestigacionProyecto) {
        this.lineaInvestigacionProyecto = lineaInvestigacionProyecto;
    }

}
