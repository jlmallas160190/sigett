/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.service;

import com.jlmallas.comun.entity.Documento;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface DocumentoService {

    void guardar(final Documento documento);

    void actualizar(final Documento documento);

    void eliminar(final Documento documento);

    Documento buscarPorId(final Documento documento);

    Documento buscar(final Documento documento);
}
