/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.webSemantica.service;

import edu.unl.sigett.webSemantica.dto.CarreraDTO;
import edu.unl.sigett.webSemantica.util.CabeceraWebSemantica;

/**
 *
 * @author jorge-luis
 */
public interface CarreraOntService {

    void read(final CabeceraWebSemantica cabecera);

    void write(final CarreraDTO carreraDTO);
}