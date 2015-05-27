/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.DocumentoCarrera;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface DocumentoCarreraService {

    void guardar(final DocumentoCarrera oficioCarrera);

    void actualizar(final DocumentoCarrera oficioCarrera);

    void remover(final DocumentoCarrera oficioCarrera);

    List<DocumentoCarrera> buscar(final DocumentoCarrera oficioCarrera);
   
}
