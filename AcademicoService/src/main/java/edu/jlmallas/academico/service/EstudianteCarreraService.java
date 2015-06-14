/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.entity.EstudianteCarrera;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface EstudianteCarreraService {

    void guardar(final EstudianteCarrera estudianteCarrera);

    void actualizar(final EstudianteCarrera estudianteCarrera);

    void eliminar(final EstudianteCarrera estudianteCarrera);

    EstudianteCarrera buscarPorId(final EstudianteCarrera estudianteCarrera);

    List<EstudianteCarrera> buscar(final EstudianteCarrera estudianteCarrera);

}
