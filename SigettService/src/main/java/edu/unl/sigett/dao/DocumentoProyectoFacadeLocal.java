/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.DocumentoProyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface DocumentoProyectoFacadeLocal {

    void create(DocumentoProyecto documentoProyecto);

    void edit(DocumentoProyecto documentoProyecto);

    void remove(DocumentoProyecto documentoProyecto);

    DocumentoProyecto find(Object id);

    List<DocumentoProyecto> findAll();

    List<DocumentoProyecto> findRange(int[] range);

    List<DocumentoProyecto> buscarPorProyecto(Long proyectoId);

    int count();

}
