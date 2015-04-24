/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.EstadoProyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface EstadoProyectoFacadeLocal {

    void create(EstadoProyecto estadoProyecto);

    void edit(EstadoProyecto estadoProyecto);

    void remove(EstadoProyecto estadoProyecto);

    EstadoProyecto find(Object id);

    List<EstadoProyecto> findAll();

    List<EstadoProyecto> findRange(int[] range);

    List<EstadoProyecto> buscarPorNombre(String nombre);

    EstadoProyecto buscarPorCodigo(String codigo);

    int count();

}
