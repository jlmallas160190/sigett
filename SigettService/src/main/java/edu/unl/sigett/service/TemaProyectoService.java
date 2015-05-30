/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.TemaProyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface TemaProyectoService {

    void guardar(final TemaProyecto temaProyecto);

    void actualizar(final TemaProyecto temaProyecto);

    void eliminar(final TemaProyecto temaProyecto);

    TemaProyecto buscarPorId(final TemaProyecto temaProyecto);

    List<TemaProyecto> buscar(final TemaProyecto temaProyecto);
}
