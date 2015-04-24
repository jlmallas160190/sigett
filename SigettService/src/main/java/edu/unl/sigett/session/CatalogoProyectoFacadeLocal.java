/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.CatalogoProyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface CatalogoProyectoFacadeLocal {

    void create(CatalogoProyecto catalogoProyecto);

    void edit(CatalogoProyecto catalogoProyecto);

    void remove(CatalogoProyecto catalogoProyecto);

    CatalogoProyecto find(Object id);

    List<CatalogoProyecto> findAll();

    List<CatalogoProyecto> findRange(int[] range);

    List<CatalogoProyecto> buscarPorCriterio(String criterio);

    List<CatalogoProyecto> buscarActivos();

    int count();

}
