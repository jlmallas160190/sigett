/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.ProyectoCarreraOferta;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface ProyectoCarreraOfertaService {

    void guardar(final ProyectoCarreraOferta proyectoCarreraOferta);

    void actualizar(final ProyectoCarreraOferta proyectoCarreraOferta);

    void eliminar(final ProyectoCarreraOferta proyectoCarreraOferta);

    ProyectoCarreraOferta buscarPorId(final ProyectoCarreraOferta proyectoCarreraOferta);

    List<ProyectoCarreraOferta> buscar(final ProyectoCarreraOferta proyectoCarreraOferta);
}
