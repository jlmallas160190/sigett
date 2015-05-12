/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.CatalogoInformeProyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface CatalogoInformeProyectoFacadeLocal {

    void create(CatalogoInformeProyecto catalogoInformeProyecto);

    void edit(CatalogoInformeProyecto catalogoInformeProyecto);

    void remove(CatalogoInformeProyecto catalogoInformeProyecto);

    CatalogoInformeProyecto find(Object id);

    List<CatalogoInformeProyecto> findAll();

    List<CatalogoInformeProyecto> findRange(int[] range);

    List<CatalogoInformeProyecto> buscarActivos();

    int count();

}
