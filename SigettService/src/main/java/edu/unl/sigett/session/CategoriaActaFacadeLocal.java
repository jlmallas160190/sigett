/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.CatalogoActa;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface CategoriaActaFacadeLocal {

    void create(CatalogoActa categoriaActa);

    void edit(CatalogoActa categoriaActa);

    void remove(CatalogoActa categoriaActa);

    CatalogoActa find(Object id);

    List<CatalogoActa> findAll();

    List<CatalogoActa> findRange(int[] range);

    CatalogoActa buscarPorCodigo(String codigo);

    int count();

}
