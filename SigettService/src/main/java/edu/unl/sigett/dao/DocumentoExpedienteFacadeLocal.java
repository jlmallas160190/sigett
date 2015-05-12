/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.DocumentoExpediente;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface DocumentoExpedienteFacadeLocal {

    void create(DocumentoExpediente documentoExpediente);

    void edit(DocumentoExpediente documentoExpediente);

    void remove(DocumentoExpediente documentoExpediente);

    DocumentoExpediente find(Object id);

    List<DocumentoExpediente> findAll();

    List<DocumentoExpediente> findRange(int[] range);

    List<DocumentoExpediente> buscarPorExpediente(Long expedienteId);

    int count();

}
