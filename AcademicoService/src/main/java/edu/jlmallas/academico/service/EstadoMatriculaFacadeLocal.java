/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.entity.EstadoMatricula;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface EstadoMatriculaFacadeLocal {

    void create(EstadoMatricula estadoMatricula);

    void edit(EstadoMatricula estadoMatricula);

    void remove(EstadoMatricula estadoMatricula);

    EstadoMatricula find(Object id);

    List<EstadoMatricula> findAll();

    List<EstadoMatricula> findRange(int[] range);

    EstadoMatricula buscarPorNombre(String nombre);

    int count();

}
