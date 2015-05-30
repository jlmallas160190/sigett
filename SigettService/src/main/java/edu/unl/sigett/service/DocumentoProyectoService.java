/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.DocumentoProyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface DocumentoProyectoService {

    void guardar(final DocumentoProyecto documentoProyecto);

    void actualizar(final DocumentoProyecto documentoProyecto);

    void eliminar(final DocumentoProyecto documentoProyecto);

    DocumentoProyecto buscarPorId(final DocumentoProyecto documentoProyecto);

    List<DocumentoProyecto> buscar(final DocumentoProyecto documentoProyecto);
}
