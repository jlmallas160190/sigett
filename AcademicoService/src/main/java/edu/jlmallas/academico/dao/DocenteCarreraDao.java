/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao;

import edu.jlmallas.academico.entity.DocenteCarrera;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface DocenteCarreraDao {

    void create(DocenteCarrera docenteCarrera);

    void edit(DocenteCarrera docenteCarrera);

    void remove(DocenteCarrera docenteCarrera);

    DocenteCarrera find(Object id);

    List<DocenteCarrera> findAll();

    List<DocenteCarrera> findRange(int[] range);

    List<DocenteCarrera> buscar(DocenteCarrera docenteCarrera);

    List<DocenteCarrera> buscarPorDocente(Long docenteId);

    int count();

}
