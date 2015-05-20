/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.EstudianteUsuario;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface EstudianteUsuarioService {

    EstudianteUsuario buscarPorEstudiante(final EstudianteUsuario estudianteUsuario);

    void guardar(final EstudianteUsuario estudianteUsuario);

    void editar(final EstudianteUsuario estudianteUsuario);

    EstudianteUsuario buscarPorId(final Long id);

}
