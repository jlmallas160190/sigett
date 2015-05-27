/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.DocumentoCarrera;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface DocumentoCarreraDao {

    void create(DocumentoCarrera oficioCarrera);

    void edit(DocumentoCarrera oficioCarrera);

    void remove(DocumentoCarrera oficioCarrera);

    DocumentoCarrera find(Object id);

    List<DocumentoCarrera> findAll();

    List<DocumentoCarrera> findRange(int[] range);

    List<DocumentoCarrera> buscar(final DocumentoCarrera oficioCarrera);

    int count();

}
