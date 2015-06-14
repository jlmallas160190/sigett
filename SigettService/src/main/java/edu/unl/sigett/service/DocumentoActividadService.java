/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.DocumentoActividad;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface DocumentoActividadService {

    void guardar(final DocumentoActividad documentoActividad);

    void actualizar(final DocumentoActividad documentoActividad);

    void eliminar(final DocumentoActividad documentoActividad);

    DocumentoActividad buscarPorId(final DocumentoActividad documentoActividad);

    List<DocumentoActividad> buscar(final DocumentoActividad documentoActividad);
}
