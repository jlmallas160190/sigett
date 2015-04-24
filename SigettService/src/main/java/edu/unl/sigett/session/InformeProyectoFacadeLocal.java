/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.InformeProyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface InformeProyectoFacadeLocal {

    void create(InformeProyecto informeProyecto);

    void edit(InformeProyecto informeProyecto);

    void remove(InformeProyecto informeProyecto);

    InformeProyecto find(Object id);

    List<InformeProyecto> findAll();

    List<InformeProyecto> findRange(int[] range);

    List<InformeProyecto> buscarPorProyecto(Long proyectoId);

    int count();

}
