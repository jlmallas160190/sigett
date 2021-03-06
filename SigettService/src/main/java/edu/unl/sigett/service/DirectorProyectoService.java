/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.DirectorProyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface DirectorProyectoService {

    void guardar(final DirectorProyecto directorProyecto);

    void actualizar(final DirectorProyecto directorProyecto);

    void eliminar(final DirectorProyecto directorProyecto);

    DirectorProyecto buscarPorId(final DirectorProyecto directorProyecto);

    List<DirectorProyecto> buscar(final DirectorProyecto directorProyecto);
}
