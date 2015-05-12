/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.CatalogoDuracion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface CatalogoDuracionFacadeLocal {

    void create(CatalogoDuracion catalogoDuracion);

    void edit(CatalogoDuracion catalogoDuracion);

    void remove(CatalogoDuracion catalogoDuracion);

    CatalogoDuracion find(Object id);

    List<CatalogoDuracion> findAll();

    List<CatalogoDuracion> findRange(int[] range);

    List<CatalogoDuracion> buscarActivos();

    int count();

}
