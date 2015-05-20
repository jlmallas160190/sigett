/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao;

import edu.jlmallas.academico.entity.EstudianteCarrera;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface EstudianteCarreraDao {

    void create(EstudianteCarrera estudianteCarrera);

    void edit(EstudianteCarrera estudianteCarrera);

    void remove(EstudianteCarrera estudianteCarrera);

    EstudianteCarrera find(Object id);

    List<EstudianteCarrera> findAll();

    List<EstudianteCarrera> findRange(int[] range);

    List<EstudianteCarrera> buscar(EstudianteCarrera estudianteCarrera);

    int count();

}
