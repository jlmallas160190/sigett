/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.Aspirante;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface AspiranteService {

    void guardar(final Aspirante aspirante);

    void actualizar(final Aspirante aspirante);

    void eliminar(final Aspirante aspirante);

    List<Aspirante> buscar(final Aspirante aspirante);

    Aspirante buscarPorId(final Long id);

}
