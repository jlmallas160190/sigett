/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.EstadoActividad;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface EstadoActividadFacadeLocal {

    void create(EstadoActividad estadoActividad);

    void edit(EstadoActividad estadoActividad);

    void remove(EstadoActividad estadoActividad);

    EstadoActividad find(Object id);

    List<EstadoActividad> findAll();

    List<EstadoActividad> findRange(int[] range);

    List<EstadoActividad> buscarActivos();

    List<EstadoActividad> buscarPorNombre(String nombre);

    int count();

}
