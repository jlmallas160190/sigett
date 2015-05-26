/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.OficioCarrera;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface OficioCarreraDao {

    void create(OficioCarrera oficioCarrera);

    void edit(OficioCarrera oficioCarrera);

    void remove(OficioCarrera oficioCarrera);

    OficioCarrera find(Object id);

    List<OficioCarrera> findAll();

    List<OficioCarrera> findRange(int[] range);

    List<OficioCarrera> buscarPorCarrera(Integer carreraId);

    OficioCarrera buscarPorTablaId(Long id, String categoriaCodigo);

    List<OficioCarrera> buscar(final OficioCarrera oficioCarrera);

    int count();

}
