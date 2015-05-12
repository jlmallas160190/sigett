/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.CatalogoOficio;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface CatalogoOficioFacadeLocal {

    void create(CatalogoOficio catalogoOficio);

    void edit(CatalogoOficio catalogoOficio);

    void remove(CatalogoOficio catalogoOficio);

    CatalogoOficio find(Object id);

    List<CatalogoOficio> findAll();

    List<CatalogoOficio> findRange(int[] range);

    CatalogoOficio buscarPorCodigo(String codigo);

    int count();

}
