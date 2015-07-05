/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.lud.service;

import edu.unl.sigett.lud.dto.ProyectoCarreraOfertaOntDTO;
import edu.unl.sigett.lud.util.CabeceraWebSemantica;

/**
 *
 * @author jorge-luis
 */
public interface ProyectoCarreraOfertaOntService {

    void read(final CabeceraWebSemantica cabecera);

    void write(final ProyectoCarreraOfertaOntDTO proyectoCarreraPeriodoDTO);
}
