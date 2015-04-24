/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.entity.EstadoEstudianteCarrera;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface EstadoEstudianteCarreraFacadeLocal {

    void create(EstadoEstudianteCarrera estadoEstudianteCarrera);

    void edit(EstadoEstudianteCarrera estadoEstudianteCarrera);

    void remove(EstadoEstudianteCarrera estadoEstudianteCarrera);

    EstadoEstudianteCarrera find(Object id);

    List<EstadoEstudianteCarrera> findAll();

    List<EstadoEstudianteCarrera> findRange(int[] range);

    EstadoEstudianteCarrera buscarPorCodigo(String codigo);

    int count();

}
