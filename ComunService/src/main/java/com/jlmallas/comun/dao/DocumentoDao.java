/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.dao;

import com.jlmallas.comun.entity.Documento;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface DocumentoDao {

    void create(Documento documento);

    void edit(Documento documento);

    void remove(Documento documento);

    Documento find(Object id);

    List<Documento> findAll();

    List<Documento> findRange(int[] range);

    Documento buscarSingle(final Documento documento);

    int count();

}
