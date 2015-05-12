/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.DocumentoActividad;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface DocumentoActividadFacadeLocal {

    void create(DocumentoActividad documentoActividad);

    void edit(DocumentoActividad documentoActividad);

    void remove(DocumentoActividad documentoActividad);

    DocumentoActividad find(Object id);

    List<DocumentoActividad> findAll();

    List<DocumentoActividad> findRange(int[] range);

    List<DocumentoActividad> buscarPorActividad(Long actividadId);

    DocumentoActividad documentoActual(Long actividadId);

    int count();

}
