/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.AutorProyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface AutorProyectoService {

    void guardar(final AutorProyecto autorProyecto);

    void editar(final AutorProyecto autorProyecto);

    void eliminar(final AutorProyecto autorProyecto);

    AutorProyecto buscarPorId(final AutorProyecto autorProyecto);

    List<AutorProyecto> buscar(final AutorProyecto autorProyecto);
}
