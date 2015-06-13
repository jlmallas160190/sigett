/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.entity.Estudiante;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface EstudianteService {

    void guardar(final Estudiante estudiante);

    void actualizar(final Estudiante estudiante);

    void eliminar(final Estudiante estudiante);

    Estudiante buscarPorId(final Estudiante estudiante);

    List<Estudiante> buscar(final Estudiante estudiante);
}
