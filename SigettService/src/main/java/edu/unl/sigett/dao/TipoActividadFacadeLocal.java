/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.TipoActividad;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface TipoActividadFacadeLocal {

    void create(TipoActividad tipoActividad);

    void edit(TipoActividad tipoActividad);

    void remove(TipoActividad tipoActividad);

    TipoActividad find(Object id);

    List<TipoActividad> findAll();

    List<TipoActividad> findRange(int[] range);

    List<TipoActividad> buscarActivos();

    List<TipoActividad> buscarPorNombre(String nombre);

    int count();

}
