/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.TemaProyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface TemaProyectoFacadeLocal {

    void create(TemaProyecto temaProyecto);

    void edit(TemaProyecto temaProyecto);

    void remove(TemaProyecto temaProyecto);

    TemaProyecto find(Object id);

    List<TemaProyecto> findAll();

    List<TemaProyecto> findRange(int[] range);

    List<TemaProyecto> buscarPorProyecto(Long proyectoId);

    int count();

}
