/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.CatalogoDocumentoExpediente;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface CatalogoDocumentoExpedienteFacadeLocal {

    void create(CatalogoDocumentoExpediente catalogoDocumentoExpediente);

    void edit(CatalogoDocumentoExpediente catalogoDocumentoExpediente);

    void remove(CatalogoDocumentoExpediente catalogoDocumentoExpediente);

    CatalogoDocumentoExpediente find(Object id);

    List<CatalogoDocumentoExpediente> findAll();

    List<CatalogoDocumentoExpediente> findRange(int[] range);

    List<CatalogoDocumentoExpediente> buscar(String criterio);

    List<CatalogoDocumentoExpediente> buscarActivos();

    List<CatalogoDocumentoExpediente> buscarObligatorios();

    int count();

}
