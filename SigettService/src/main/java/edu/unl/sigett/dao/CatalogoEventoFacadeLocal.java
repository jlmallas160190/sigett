/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.CatalogoEvento;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface CatalogoEventoFacadeLocal {

    void create(CatalogoEvento catalogoEvento);

    void edit(CatalogoEvento catalogoEvento);

    void remove(CatalogoEvento catalogoEvento);

    CatalogoEvento find(Object id);

    List<CatalogoEvento> findAll();

    List<CatalogoEvento> findRange(int[] range);

    CatalogoEvento buscarPorCodigo(String codigo);

    int count();

}
