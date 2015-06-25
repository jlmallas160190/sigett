/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.webSemantica.service;

import edu.unl.sigett.webSemantica.dto.ProyectoOntDTO;
import edu.unl.sigett.webSemantica.util.CabeceraWebSemantica;
import java.util.List;

/**
 *
 * @author jorge-luis
 */
public interface ProyectoOntService {

    void read(final CabeceraWebSemantica cabecera);

    void write(final ProyectoOntDTO proyectoDTO);

    List<ProyectoOntDTO> buscar(final ProyectoOntDTO proyectoOntDTO);
}
