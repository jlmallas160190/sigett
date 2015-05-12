/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.CatalogoDocumentoProyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface CatalogoDocumentoProyectoFacadeLocal {

    void create(CatalogoDocumentoProyecto catalogoDocumentoProyecto);

    void edit(CatalogoDocumentoProyecto catalogoDocumentoProyecto);

    void remove(CatalogoDocumentoProyecto catalogoDocumentoProyecto);

    CatalogoDocumentoProyecto find(Object id);

    List<CatalogoDocumentoProyecto> findAll();

    List<CatalogoDocumentoProyecto> findRange(int[] range);

    List<CatalogoDocumentoProyecto> buscarActivos();

    int count();

}
